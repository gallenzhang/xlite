package com.xlite.codec;

import com.xlite.protocol.RequestCodec;
import com.xlite.remoting.exchange.Request;
import com.xlite.rpc.RpcInvocation;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/1
 **/
@ChannelHandler.Sharable
public class RequestCodecHandler extends MessageToMessageCodec<ByteBuf,RpcInvocation> {

    public static final RequestCodecHandler INSTANCE = new RequestCodecHandler();

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcInvocation msg, List<Object> out) throws Exception {
        ByteBuf byteBuf = ctx.channel().alloc().ioBuffer();
        RequestCodec.INSTANCE.encode(byteBuf,msg);
        out.add(byteBuf);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        out.add(RequestCodec.INSTANCE.decode(msg));
    }
}
