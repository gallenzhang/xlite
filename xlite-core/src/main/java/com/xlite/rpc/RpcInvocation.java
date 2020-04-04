package com.xlite.rpc;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * RpcInvocation
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/1
 **/
public class RpcInvocation implements Invocation,Serializable {

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 方法参数
     */
    private Class<?>[] parameterTypes;

    /**
     * 调用参数
     */
    private Object[] arguments;

    /**
     * 附加参数
     */
    private Map<String, String> attachments;

    public RpcInvocation(Method method, Object[] arguments){
        this(method.getName(),method.getParameterTypes(),arguments,null);
    }

    public RpcInvocation(String methodName, Class<?>[] parameterTypes, Object[] arguments, Map<String, String> attachments) {
        this.methodName = methodName;
        this.parameterTypes = parameterTypes == null ? new Class<?>[0] : parameterTypes;
        this.arguments = arguments == null ? new Object[0] : arguments;
        this.attachments = attachments == null ? new HashMap<>() : attachments;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    @Override
    public Map<String, String> getAttachments() {
        return attachments;
    }

    @Override
    public String getAttachment(String key) {
        if(attachments == null){
            return null;
        }
        return attachments.get(key);
    }

    @Override
    public String getAttachment(String key, String defaultValue) {
        if(attachments == null){
            return defaultValue;
        }

        String value = attachments.get(key);
        if(value == null || value.length() == 0){
            return defaultValue;
        }
        return value;
    }

    public void setAttachments(Map<String, String> attachments) {
        this.attachments = attachments;
    }

    @Override
    public String toString() {
        return "RpcInvocation{" +
                "methodName='" + methodName + '\'' +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                ", arguments=" + Arrays.toString(arguments) +
                ", attachments=" + attachments +
                '}';
    }
}
