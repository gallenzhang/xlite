package com.xlite.remoting;

import com.xlite.remoting.transport.Channel;
import com.xlite.remoting.transport.MessageHandler;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p>
 * 业务处理
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/1
 **/
@io.netty.channel.ChannelHandler.Sharable
public class NettyServerHandler extends ChannelDuplexHandler {

    private ThreadPoolExecutor threadPoolExecutor;

    private MessageHandler messageHandler;

    private Channel channel;

    public NettyServerHandler(ThreadPoolExecutor threadPoolExecutor, MessageHandler messageHandler, Channel channel) {
        this.threadPoolExecutor = threadPoolExecutor;
        this.messageHandler = messageHandler;
        this.channel = channel;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
    }
}
