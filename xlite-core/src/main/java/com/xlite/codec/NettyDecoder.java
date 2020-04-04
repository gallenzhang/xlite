package com.xlite.codec;

import com.xlite.codec.impl.DefaultRpcCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * <p>
 * netty decoder
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/4
 **/
public class NettyDecoder extends ByteToMessageDecoder {


    private Codec codec = new DefaultRpcCodec();

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);

        //请求字节数组解码
        Object result = codec.decode(bytes);
        list.add(result);
    }
}
