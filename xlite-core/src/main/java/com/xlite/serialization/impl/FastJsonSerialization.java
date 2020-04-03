package com.xlite.serialization.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xlite.serialization.Serialization;

import java.io.IOException;

/**
 * <p>
 * FastJson 序列化
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/2
 **/
public class FastJsonSerialization implements Serialization {


    @Override
    public byte[] serialize(Object object) throws IOException {
        SerializeWriter out = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(out);
        serializer.config(SerializerFeature.WriteEnumUsingToString,true);
        serializer.config(SerializerFeature.WriteClassName,true);
        serializer.write(object);
        return out.toBytes("UTF-8");
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException{
        return JSON.parseObject(new String(bytes),clazz);
    }

    @Override
    public byte getSerializationId() {
        return (byte) 1;
    }
}
