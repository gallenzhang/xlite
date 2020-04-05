package com.xlite.remoting;

import com.xlite.codec.NettyDecoder;
import com.xlite.codec.NettyEncoder;
import com.xlite.codec.XliteFrameDecoder;
import com.xlite.common.ChannelState;
import com.xlite.common.XliteConstant;
import com.xlite.remoting.exchange.Request;
import com.xlite.remoting.exchange.Response;
import com.xlite.remoting.transport.AbstractServer;
import com.xlite.remoting.transport.MessageHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Netty服务端
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/1
 **/
public class NettyServer extends AbstractServer {

    private ServerBootstrap bootstrap;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    /**
     * 业务处理线程池
     */
    private ThreadPoolExecutor workThreadPool = null;

    /**
     * server Socket channel
     */
    private Channel channel = null;

    /**
     * channel manage
     */
    private NettyServerChannelManage channelManage = null;

    /**
     * 消息处理器
     */
    private MessageHandler messageHandler;


    public NettyServer(MessageHandler messageHandler){
        this.messageHandler = messageHandler;
    }


    @Override
    public Response request(Request request) {
        return null;
    }

    @Override
    public boolean open() {
        bootstrap = new ServerBootstrap();

        bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("NettyServerBoss", true));
        workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors(), new DefaultThreadFactory("NettyServerWorker", true));

        //初始化业务处理线程池
        if(workThreadPool == null){
            workThreadPool = new ThreadPoolExecutor(XliteConstant.MIN_WORKER_THREAD,XliteConstant.MAX_WORKER_THREAD,
                    XliteConstant.DEFAULT_MAX_IDLE_TIME,TimeUnit.MILLISECONDS,new ArrayBlockingQueue<>(XliteConstant.WORKER_QUEUE_SIZE),
                    new DefaultThreadFactory("NettyServer-" + XliteConstant.DEFAULT_PORT));
            workThreadPool.prestartAllCoreThreads();
        }

        channelManage = new NettyServerChannelManage(XliteConstant.MAX_SERVER_CONNECTION);
        final NettyChannelHandler handler = new NettyChannelHandler(NettyServer.this,messageHandler,workThreadPool);

        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                .childOption(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast("channel_manage",channelManage)
                                .addLast("encoder",new NettyEncoder())
                                .addLast("frame_decoder",new XliteFrameDecoder())
                                .addLast("decoder",new NettyDecoder())
                                .addLast("handler",handler);
                    }
                });

        //绑定
        ChannelFuture channelFuture = bootstrap.bind(XliteConstant.DEFAULT_PORT);
        channelFuture.syncUninterruptibly();
        channel = channelFuture.channel();

        state = ChannelState.ALIVE;
        return true;
    }

    @Override
    public void close() {
        if(channel != null){
            channel.close();
        }

        if(bootstrap != null){
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
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
