package com.xlite.remoting;

import com.xlite.codec.RequestCodecHandler;
import com.xlite.codec.RequestFrameDecoder;
import com.xlite.protocol.xlite.Constants;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;

/**
 * <p>
 * Netty服务端
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/1
 **/
public class NettyServer {

    private ServerBootstrap bootstrap;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private Channel channel = null;


    public NettyServer(){
        doOpen();
    }

    public void doOpen() {
        bootstrap = new ServerBootstrap();

        bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("NettyServerBoss", true));
        workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors(), new DefaultThreadFactory("NettyServerWorker", true));

        final NettyServerHandler nettyServerHandler = new NettyServerHandler();

        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                .childOption(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new RequestFrameDecoder())
                                .addLast(RequestCodecHandler.INSTANCE)
                                .addLast(nettyServerHandler);
                    }
                });

        //绑定
        ChannelFuture channelFuture = bootstrap.bind(Constants.DEFAULT_PORT);
        channelFuture.syncUninterruptibly();
        channel = channelFuture.channel();
    }

    /**
     * 关闭服务器
     */
    protected void doClose(){
        if(channel != null){
            channel.close();
        }

        if(bootstrap != null){
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
