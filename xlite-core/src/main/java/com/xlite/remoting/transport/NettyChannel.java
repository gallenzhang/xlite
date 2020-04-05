package com.xlite.remoting.transport;

import com.xlite.codec.Codec;
import com.xlite.codec.impl.DefaultRpcCodec;
import com.xlite.common.ChannelState;
import com.xlite.common.CodecUtils;
import com.xlite.remoting.NettyClient;
import com.xlite.remoting.exchange.Request;
import com.xlite.remoting.exchange.Response;
import com.xlite.rpc.RpcException;
import com.xlite.rpc.RpcResponse;
import io.netty.channel.ChannelFuture;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 *
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/5
 **/
public class NettyChannel implements Channel{

    private NettyClient nettyClient;
    private volatile ChannelState state = ChannelState.UN_INIT;

    private InetSocketAddress remote = null;
    private InetSocketAddress local = null;
    private io.netty.channel.Channel channel = null;
    private ReentrantLock lock = new ReentrantLock();
    private Codec codec;


    public NettyChannel(NettyClient nettyClient){
        this.nettyClient = nettyClient;

        String[] urlSplit = nettyClient.getUrl().split(":");
        remote = new InetSocketAddress(urlSplit[0],Integer.parseInt(urlSplit[1]));

        codec = new DefaultRpcCodec();
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return local;
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return remote;
    }

    @Override
    public Response request(Request request) {
        byte[] bytes = CodecUtils.encodeToBytes(codec,request);
        ChannelFuture writeFuture = this.channel.writeAndFlush(bytes);

        boolean result = writeFuture.awaitUninterruptibly(1000,TimeUnit.MILLISECONDS);
        if(result && writeFuture.isSuccess()){
            RpcResponse response = new RpcResponse(request.getRequestId());
            response.setValue(null);
            return response;
        }

        writeFuture.cancel(true);

        if(writeFuture.cause() == null){
            throw new RpcException("NettyChannel send request to server Error: url=" + nettyClient.getUrl() + " local=" + local,
                    writeFuture.cause());
        }else {
            throw new RpcException("NettyChannel send request to server Error: url=" + nettyClient.getUrl() + " local=" + local);
        }
    }

    @Override
    public synchronized boolean open() {
        if(isAvailable()){
            return true;
        }

        ChannelFuture channelFuture;
        try {

            long start = System.currentTimeMillis();
            channelFuture = nettyClient.getBootstrap().connect(remote);


            boolean result = channelFuture.awaitUninterruptibly(1000,TimeUnit.MILLISECONDS);
            boolean success = channelFuture.isSuccess();
            if(result && success){
                channel = channelFuture.channel();

                if(channel.localAddress() != null && channel.localAddress() instanceof InetSocketAddress){
                    local = (InetSocketAddress) channel.localAddress();
                }

                state = ChannelState.ALIVE;
                return true;
            }

            boolean connected = false;
            if(channelFuture.channel() != null){
                connected = channelFuture.channel().isActive();
            }

            if(channelFuture.cause() != null){
                channelFuture.cancel(true);

                throw new RpcException("NettyChannel failed to connect to server,url: " + nettyClient.getUrl() + ", result:" + result + ", success: " +
                    success + ", connected: " + connected,channelFuture.cause());
            }else {
                channelFuture.cancel(true);
                throw new RpcException("NettyChannel failed to connect to server timeout ,url: " + nettyClient.getUrl() + ", cost: " +
                        (System.currentTimeMillis() - start) + ", result:" + result + ", success: " +
                        success + ", connected: " + connected);
            }

        }catch (Exception e){
            throw new RpcException("NettyChannel failed to connect to server, url: " + nettyClient.getUrl(),e);
        }
    }

    @Override
    public synchronized void close() {
        try {
            state = ChannelState.CLOSE;

            if(channel != null){
                channel.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean isClosed() {
        return state.isCloseState();
    }

    @Override
    public boolean isAvailable() {
        return state.isAliveState() && channel != null && channel.isActive();
    }

    public boolean isReconnect(){
        return state.isInitState();
    }

    public void reconnect() {
        state = ChannelState.INIT;
    }

    public ReentrantLock getLock() {
        return lock;
    }



}
