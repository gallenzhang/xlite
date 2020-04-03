package com.xlite.rpc;

import com.xlite.remoting.exchange.Request;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>
 * RpcRequest
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/2
 **/
public class RpcRequest implements Request ,Serializable {

    /**
     * 记录请求id
     */
    private static final AtomicLong INVOKE_ID = new AtomicLong(0);

    /**
     * 请求id
     */
    private long requestId;

    /**
     * 接口名称
     */
    private String interfaceName;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 方法参数签名
     */
    private String parametersDesc;

    /**
     * 方法调用实参
     */
    private Object[] arguments;

    /**
     * 附加参数
     */
    private Map<String,String> attachments;

    /**
     * 生成一个自增ID
     */
    public RpcRequest(){
        requestId = INVOKE_ID.getAndIncrement();
    }


    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setParametersDesc(String parametersDesc) {
        this.parametersDesc = parametersDesc;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    public void setAttachments(Map<String, String> attachments) {
        this.attachments = attachments;
    }

    @Override
    public String getInterfaceName() {
        return this.interfaceName;
    }

    @Override
    public String getMethodName() {
        return this.methodName;
    }

    @Override
    public String getParametersDesc() {
        return this.parametersDesc;
    }

    @Override
    public Object[] getArguments() {
        return this.arguments;
    }

    @Override
    public Map<String, String> getAttachments() {
        return this.attachments == null ? new HashMap<String, String>() : this.attachments;
    }

    @Override
    public void setAttachment(String key, String value) {
        if(this.attachments == null){
            this.attachments = new HashMap<>();
        }
        this.attachments.put(key,value);
    }

    @Override
    public String getAttachment(String key) {
        if(this.attachments == null){
            return null;
        }
        return this.attachments.get(key);
    }

    @Override
    public String getAttachment(String key, String defaultValue) {
        if(this.attachments == null || this.attachments.get(key) == null){
            return defaultValue;
        }
        return attachments.get(key);
    }

    @Override
    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId){
        this.requestId = requestId;
    }

}
