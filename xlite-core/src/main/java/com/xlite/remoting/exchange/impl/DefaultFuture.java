package com.xlite.remoting.exchange.impl;

import com.xlite.common.XliteConstant;
import com.xlite.remoting.exchange.Request;
import com.xlite.remoting.exchange.Response;
import com.xlite.remoting.exchange.ResponseCallback;
import com.xlite.remoting.exchange.ResponseFuture;
import com.xlite.remoting.transport.Channel;
import com.xlite.rpc.RpcResponse;
import com.xlite.rpc.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * default future
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/5
 **/
public class DefaultFuture implements ResponseFuture {

    private static final Logger logger = LoggerFactory.getLogger(DefaultFuture.class);

    private static final Map<Long,DefaultFuture> FUTURE_MAP = new ConcurrentHashMap<>();
    private static final Map<Long,Channel> CHANNEL_MAP = new ConcurrentHashMap<>();


    static {
        Thread thread = new Thread(new InvocationTimeoutScanTask(),"XliteResponseTimeoutScanTimer");
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * request id
     */
    private final long requestId;

    /**
     * request
     */
    private final Request request;

    /**
     * channel
     */
    private final Channel channel;

    /**
     * invoke timeout
     */
    private final int timeout;

    /**
     * invoke response
     */
    private volatile Response response;

    /**
     * response callback
     */
    private volatile ResponseCallback callback;

    /**
     * lock and condition
     */
    private final Lock lock = new ReentrantLock();
    private final Condition done = lock.newCondition();

    /**
     * record start time
     */
    private final long start = System.currentTimeMillis();

    public DefaultFuture(Channel channel, Request request, int timeout){
        this.channel = channel;
        this.requestId = request.getRequestId();
        this.request = request;
        this.timeout = timeout;

        //加入map中
        FUTURE_MAP.put(requestId,this);
        CHANNEL_MAP.put(requestId,channel);
    }

    /**
     * get timeout
     * @return
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * get request id
     * @return
     */
    public long getRequestId() {
        return requestId;
    }

    /**
     * get start
     * @return
     */
    private long getStartTimestamp() {
        return start;
    }

    @Override
    public Object get() throws TimeoutException {
        return get(timeout);
    }

    /**
     * get channel
     * @return
     */
    public Channel getChannel() {
        return channel;
    }

    @Override
    public Object get(int timeout) throws TimeoutException {
        if(timeout <= 0){
            timeout = XliteConstant.DEFAULT_INVOKE_TIME_OUT;
        }

        if(!isDone()){
            long start = System.currentTimeMillis();
            lock.lock();

            try {
                while (!isDone()) {
                    done.await(timeout, TimeUnit.MILLISECONDS);
                    if(isDone() || System.currentTimeMillis() - start >= timeout){
                        break;
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }finally {
                lock.unlock();
            }

            if(!isDone()){
                throw new TimeoutException("invoke service timeout");
            }

        }
        return returnFromResponse();
    }

    /**
     * return from response
     * @return
     */
    private Object returnFromResponse(){
        Response res = response;
        if(res == null){
            throw new IllegalStateException("response cannot be null");
        }

        return response;
    }

    @Override
    public void setCallback(ResponseCallback callback) {
        if(isDone()){
            invokeCallback(callback);
        }else {
            boolean isDone = false;
            lock.lock();
            try {
                if(!isDone()){
                    this.callback = callback;
                }else {
                    isDone = true;
                }

            }finally {
                lock.unlock();
            }

            if(isDone){
                invokeCallback(callback);
            }
        }
    }

    /**
     * invoke callback
     * @param callback
     */
    private void invokeCallback(ResponseCallback callback){
        ResponseCallback c = callback;

        if(c == null){
            throw new NullPointerException("callback cannot be null");
        }

        c = null;

        Response res = response;
        if(res == null){
            throw new IllegalStateException("response cannot be null.");
        }

        c.done(res.getValue());
    }

    /**
     * receive
     * @param channel
     * @param response
     */
    public static void receive(Channel channel, Response response){
        try {
            DefaultFuture future = FUTURE_MAP.remove(response.getRequestId());
            if(future != null){
                future.doReceived(response);
            }else {
                logger.warn("The timeout response finally returned at "
                        + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                        + ", response " + response
                        + (channel == null ? "" : ", channel: " + channel.getLocalAddress()
                        + "->" + channel.getRemoteAddress()));
            }
        }finally {
            CHANNEL_MAP.remove(response.getRequestId());
        }
    }

    /**
     * do receive response
     * @param response
     */
    private void doReceived(Response response) {
        lock.lock();
        try {
            this.response = response;
            if(done != null){
                done.signal();
            }
        }finally {
            lock.unlock();
        }

        if(callback != null){
            invokeCallback(callback);
        }
    }

    @Override
    public boolean isDone() {
        return response != null;
    }


    /**
     * cancel
     */
    public void cancel(){
        RpcResponse response = new RpcResponse();
        response.setRequestId(requestId);
        this.response = response;
        FUTURE_MAP.remove(requestId);
        CHANNEL_MAP.remove(requestId);
    }

    /**
     * 调用超时扫描任务
     */
    private static class InvocationTimeoutScanTask implements Runnable{

        @Override
        public void run() {
            while (true){
                try {
                    for (DefaultFuture future : FUTURE_MAP.values()){
                        if(future == null || future.isDone()){
                            continue;
                        }

                        //调用超时
                        if((System.currentTimeMillis() - future.getStartTimestamp() > future.getTimeout())){
                            //create response
                            RpcResponse response = new RpcResponse();
                            response.setRequestId(future.getRequestId());
                            response.setException(new TimeoutException("invoke service timeout"));

                            //handle response
                            DefaultFuture.receive(future.getChannel(),response);
                        }
                    }

                    Thread.sleep(30);

                }catch (Throwable e){
                    logger.error("Exception when scan the timeout invocation of remoting.",e);
                }
            }
        }
    }
}
