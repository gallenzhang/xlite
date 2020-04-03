package com.xlite.serialization.impl;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.xlite.serialization.Serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * <p>
 * Hessian2 序列化
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/2
 **/
public class Hessian2Serialization implements Serialization {

    @Override
    public byte[] serialize(Object object) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Hessian2Output out = new Hessian2Output(bos);
        out.writeObject(object);
        out.flush();
        return bos.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Hessian2Input input = new Hessian2Input(bis);
        return (T) input.readObject(clazz);
    }

    @Override
    public byte getSerializationId() {
        return (byte) 2;
    }
}
