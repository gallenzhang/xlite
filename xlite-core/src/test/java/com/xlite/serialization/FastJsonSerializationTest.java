package com.xlite.serialization;

import com.xlite.bean.PersonBean;
import com.xlite.serialization.impl.FastJsonSerialization;
import com.xlite.serialization.impl.Hessian2Serialization;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * <p>
 * FastJson 序列化测试
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/4
 **/

public class FastJsonSerializationTest {

    private Serialization serialization = new FastJsonSerialization();
    private PersonBean personBean = new PersonBean("欧阳铁柱",27);

    @Test
    public void testSerialize() throws IOException {
        System.out.println("序列化后字节数组长度: " + serialization.serialize(personBean).length);
    }

    @Test
    public void testDeserialize() throws IOException {
        System.out.println(serialization.deserialize(serialization.serialize(personBean),PersonBean.class));
    }

    @Test
    public void testGetSerializationId(){
        Assert.assertEquals(serialization.getSerializationId(),1);
    }
}
