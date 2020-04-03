package com.xlite.codec.impl;

import com.xlite.common.ReflectUtil;
import com.xlite.serialization.Serialization;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 抽象编解码类
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/3
 **/
public abstract class AbstractRpcCodec {

    /**
     * 序列化对象
     * @param output
     * @param serialization
     * @param object
     * @throws IOException
     */
    protected void serialize(ObjectOutput output, Serialization serialization, Object object) throws IOException {
        if(object == null){
            output.writeObject(null);
        }else {
            output.writeObject(serialization.serialize(object));
        }
    }

    /**
     * 反序列化对象
     * @param bytes
     * @param classType
     * @param serialization
     * @return
     * @throws IOException
     */
    protected Object deserialize(byte[] bytes,Class<?> classType,Serialization serialization) throws IOException {
        if(bytes == null){
            return null;
        }

        return serialization.deserialize(bytes,classType);
    }

    /**
     * 解码方法调用参数
     * @param input
     * @param parametersDesc
     * @param serialization
     * @return
     */
    public Object[] decodeArgument(ObjectInput input, String parametersDesc, Serialization serialization) throws ClassNotFoundException, IOException {
        if(parametersDesc == null || parametersDesc.length() == 0){
            return null;
        }

        //参数Class
        Class<?>[] classTypes = ReflectUtil.forNames(parametersDesc);

        Object[] params = new Object[classTypes.length];
        for(int i = 0; i < classTypes.length; i++){
            params[i] = serialization.deserialize((byte[]) input.readObject(),classTypes[i]);
        }

        return params;
    }

    /**
     * 解码附加参数
     * @param input
     * @return
     * @throws IOException
     */
    protected Map<String,String> decodeAttachment(ObjectInput input) throws IOException {
        int size = input.readInt();
        if(size <= 0){
            return null;
        }

        Map<String,String> attachments = new HashMap<>(size);
        for (int i = 0; i < size; i++){
            attachments.put(input.readUTF(),input.readUTF());
        }
        return attachments;
    }
}
