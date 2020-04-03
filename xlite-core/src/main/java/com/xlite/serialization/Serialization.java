package com.xlite.serialization;

import java.io.IOException;

/**
 * <p>
 * 序列化接口
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/2
 **/
public interface Serialization {

    /**
     * 序列化
     * @param object
     * @return
     */
    byte[] serialize(Object object) throws IOException;


    /**
     * 反序列化
     * @param bytes
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException;

    /**
     * 序列化标识
     * @return
     */
    byte getSerializationId();
}
