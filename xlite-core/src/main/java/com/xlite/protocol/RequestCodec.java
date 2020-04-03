package com.xlite.protocol;

import com.xlite.rpc.RpcInvocation;
import io.netty.buffer.ByteBuf;

/**
 * <p>
 *  请求编解码
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/1
 **/
public class RequestCodec {

    public static final int MAGIC_NUMBER = 0x12345678;

    public static final RequestCodec INSTANCE = new RequestCodec();

    public RpcInvocation decode(ByteBuf byteBuf){
        //跳过魔数
        byteBuf.skipBytes(4);

        //跳过版本号
        byteBuf.skipBytes(1);

        //序列化算法
        byte serialize = byteBuf.readByte();

        //请求/响应
        byte type = byteBuf.readByte();
        //响应状态
        byte status = byteBuf.readByte();

        Long requestId = byteBuf.readLong();

        //数据包长度
        int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        return null;
    }

    public void encode(ByteBuf byteBuf,RpcInvocation request){

    }

}
