package com.xlite.common;

import com.xlite.codec.Codec;
import com.xlite.remoting.exchange.Response;
import com.xlite.rpc.RpcException;

import java.io.IOException;

/**
 * <p>
 * codec util
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/4
 **/
public class CodecUtils {

    /**
     * 对象编码成字节数组
     * @param codec
     * @param msg
     * @return
     */
    public static byte[] encodeToBytes(Codec codec, Object msg) {
        try {
            return codec.encode(msg);
        }catch (IOException e){
            throw new XliteFrameworkException("encode error: isResponse=" + (msg instanceof Response),e, RpcException.ENCODE_EXCEPTION);
        }
    }
}
