package com.xlite.codec;

import com.xlite.protocol.RequestCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * <p>
 * 请求解码校验
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/1
 **/
public class RequestFrameDecoder extends LengthFieldBasedFrameDecoder {

    private static final int LENGTH_FIELD_OFFSET = 16;
    private static final int LENGTH_FIELD_LENGTH = 4;

    public RequestFrameDecoder(){
        super(Integer.MAX_VALUE,LENGTH_FIELD_OFFSET,LENGTH_FIELD_LENGTH);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        //验证协议头，不通过则断开连接
        if(in.getInt(in.readerIndex()) != RequestCodec.MAGIC_NUMBER){
            ctx.channel().close();
            return null;
        }
        return super.decode(ctx, in);
    }
}
