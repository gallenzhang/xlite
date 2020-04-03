package com.xlite.serialization;

import com.xlite.bean.PersonBean;
import com.xlite.serialization.impl.Hessian2Serialization;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * <p>
 * Hessian2 序列化测试
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/4
 **/
public class Hessian2SerializationTest {

    private Serialization serialization = new Hessian2Serialization();
    private PersonBean personBean = new PersonBean("欧阳建国",30);

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
        Assert.assertEquals(serialization.getSerializationId(),2);
    }
}
