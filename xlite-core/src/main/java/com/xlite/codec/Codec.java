package com.xlite.codec;

import java.io.IOException;

/**
 * <p>
 * 编解码
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/2
 **/
public interface Codec {

    /**
     * 编码
     * @param msg
     * @return
     * @throws IOException
     */
    byte[] encode(Object msg) throws IOException;

    /**
     * 解码
     * @param bytes
     * @return
     * @throws IOException
     */
    Object decode(byte[] bytes) throws IOException;
}
