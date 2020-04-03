package com.xlite.codec;

import com.xlite.bean.PersonBean;
import com.xlite.codec.impl.DefaultRpcCodec;
import com.xlite.common.ReflectUtil;
import com.xlite.remoting.exchange.Request;
import com.xlite.remoting.exchange.Response;
import com.xlite.rpc.RpcRequest;
import com.xlite.rpc.RpcResponse;
import com.xlite.service.TestService;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>
 * 编解码测试类
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/4
 **/
public class DefaultRpcCodecTest {

    private Codec codec = new DefaultRpcCodec();

    private Request request = null;
    private Request emptyParamRequest = null;
    private Response response = null;
    private PersonBean personBean = new PersonBean("吴轩",46);

    @Before
    public void request(){
        String methodName = "testMethod";

        request = new RpcRequest();
        ((RpcRequest) request).setInterfaceName(TestService.class.getName());
        ((RpcRequest) request).setMethodName(methodName);


        Method method = null;
        for(Method m : TestService.class.getDeclaredMethods()){
            if(m.getName().equals(methodName)){
                method = m;
                break;
            }
        }
        String parametersDesc = ReflectUtil.getMethodParamDesc(method);
        ((RpcRequest) request).setParametersDesc(parametersDesc);
        ((RpcRequest) request).setArguments(new Object[]{personBean,new Date(),33518L,new int[][]{}});

        Map<String,String > attachments = new HashMap<>();
        attachments.put("version","1.0");
        attachments.put("group","test");
        ((RpcRequest) request).setAttachments(attachments);
    }

    @Before
    public void emptyParamRequest(){
        String methodName = "buildPerson";

        emptyParamRequest = new RpcRequest();
        ((RpcRequest) emptyParamRequest).setInterfaceName(TestService.class.getName());
        ((RpcRequest) emptyParamRequest).setMethodName(methodName);

        Method method = null;
        for(Method m : TestService.class.getDeclaredMethods()){
            if(m.getName().equals(methodName)){
                method = m;
                break;
            }
        }
        String parametersDesc = ReflectUtil.getMethodParamDesc(method);
        ((RpcRequest) emptyParamRequest).setParametersDesc(parametersDesc);
        ((RpcRequest) emptyParamRequest).setArguments(new Object[0]);

    }

    @Before
    public void buildResponse(){
        response = new RpcResponse();
        ((RpcResponse) response).setProcessTime(1000);
        ((RpcResponse) response).setRequestId(new AtomicLong().getAndIncrement());
        ((RpcResponse) response).setValue("响应成功啦");

    }

    @After
    public void destroy(){
        request = null;
        response = null;
    }

    /**
     * request 编码
     */
    @Test
    public void testEncodeRequest() throws IOException {
        byte[] bytes = codec.encode(request);
        System.out.println("request编码后长度：" + bytes.length);
    }

    /**
     * request 编码
     */
    @Test
    public void testEmptyParamEncodeRequest() throws IOException {
        byte[] bytes = codec.encode(emptyParamRequest);
        System.out.println("request编码后长度：" + bytes.length);
    }

    /**
     * request 解码
     */
    @Test
    public void testEmptyParamDecodeRequest() throws IOException {
        System.out.println(codec.decode(codec.encode(emptyParamRequest)));
    }


    /**
     * request 解码
     */
    @Test
    public void testDecodeRequest() throws IOException {
        System.out.println(codec.decode(codec.encode(request)));
    }

    /**
     * response 编码
     */
    @Test
    public void testEncodeResponse() throws IOException {
        byte[] bytes = codec.encode(response);
        System.out.println("response编码后长度：" + bytes.length);
    }

    /**
     * response 解码
     */
    @Test
    public void testDecodeResponse() throws IOException {
        System.out.println(codec.decode(codec.encode(response)));
    }
}
