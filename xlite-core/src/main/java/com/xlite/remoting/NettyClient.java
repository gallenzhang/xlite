package com.xlite.remoting;

import com.xlite.codec.NettyDecoder;
import com.xlite.codec.NettyEncoder;
import com.xlite.codec.XliteFrameDecoder;
import com.xlite.common.ChannelState;
import com.xlite.common.XliteConstant;
import com.xlite.remoting.exchange.Request;
import com.xlite.remoting.exchange.Response;
import com.xlite.remoting.exchange.impl.DefaultFuture;
import com.xlite.remoting.transport.AbstractClient;
import com.xlite.remoting.transport.Channel;
import com.xlite.remoting.transport.NettyChannel;
import com.xlite.rpc.RpcException;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;

/**
 * <p>
 * Netty客户端
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/1
 **/
public class NettyClient extends AbstractClient {

    /**
     * bootstrap
     */
    private Bootstrap bootstrap;

    /**
     * nioEventLoopGroup
     */
    private static final NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(XliteConstant.DEFAULT_IO_THREADS,
            new DefaultThreadFactory("NettyClientWorker", true));

    public NettyClient(String url){
        super(url);
    }

    public Bootstrap getBootstrap() {
        return bootstrap;
    }

    @Override
    public void heartbeat(Request request) {

    }


    @Override
    public Response request(Request request) {
        if(!isAvailable()){
            throw new RpcException("NettyChannel is unavailable: url=" + url);
        }

        Response response;
        try {
            Channel channel = getChannel();

            if(channel == null){
                return null;
            }

            DefaultFuture future = new DefaultFuture(channel,request,XliteConstant.DEFAULT_INVOKE_TIME_OUT);
            try {
                //真正发送netty 请求
                channel.request(request);
            }catch (RpcException e){
                future.cancel();
                throw  e;
            }

            response = (Response) future.get();
            return response;

        }catch (Exception e){
            throw new RpcException("NettyClient request Error: url=" + url,e);
        }
    }

    @Override
    public boolean open() {
        if(isAvailable()){
            return true;
        }

        bootstrap = new Bootstrap();
        bootstrap.group(nioEventLoopGroup)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .channel(NioSocketChannel.class);

        bootstrap.handler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline()
                        .addLast("encoder", new NettyEncoder())
                        .addLast("frame_decoder",new XliteFrameDecoder())
                        .addLast("decoder", new NettyDecoder())
                        .addLast("handler", new NettyChannelHandler(NettyClient.this, (channel, message) -> {
                            //收到服务端响应
                            Response response = (Response) message;
                            if(response != null){
                                DefaultFuture.receive(channel,response);
                            }
                            return null;
                        }));
            }
        });


        //初始化连接
        initChannel();

        //设置可用状态
        state = ChannelState.ALIVE;

        return true;
    }

    /**
     * 初始化连接
     */
    private void initChannel() {
        channel = new NettyChannel(this);
        try {
            channel.open();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        if(state.isCloseState()){
            return;
        }

        state = ChannelState.CLOSE;
    }

    @Override
    public boolean isClosed() {
        return state.isCloseState();
    }

    @Override
    public boolean isAvailable() {
        return state.isAliveState();
    }
}
