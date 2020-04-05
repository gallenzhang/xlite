package com.xlite.remoting.transport;

import com.xlite.common.ChannelState;
import com.xlite.remoting.exchange.Request;
import com.xlite.rpc.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 *
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/5
 **/
public abstract class AbstractClient implements Client {

    private static final Logger logger = LoggerFactory.getLogger(AbstractClient.class);

    /**
     * channel
     */
    protected Channel channel;

    /**
     * client state
     */
    protected volatile ChannelState state = ChannelState.UN_INIT;

    protected String url;

    public AbstractClient(String url){
        this.url = url;
    }


    @Override
    public void heartbeat(Request request) {

    }

    /**
     * 获取channel
     * @return
     */
    protected Channel getChannel(){
        if(!channel.isAvailable()){
            //重连
            NettyChannel nettyChannel = (NettyChannel) channel;
            ReentrantLock lock = nettyChannel.getLock();
            if(lock.tryLock()){
                try {
                    if(!nettyChannel.isAvailable() && !nettyChannel.isReconnect()){
                        nettyChannel.reconnect();

                        //netty channel断开重连
                        nettyChannel.close();
                        nettyChannel.open();
                    }
                }catch (Exception e){
                    logger.error("get channel error: ");
                }finally {
                    lock.unlock();
                }
            }
        }

        //返回可用channel
        if(channel.isAvailable()){
            return channel;
        }

        throw new RpcException(this.getClass().getSimpleName() + " getChannel Error: url=" + url);
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return channel.getLocalAddress();
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return channel.getRemoteAddress();
    }

    public String getUrl() {
        return url;
    }
}
