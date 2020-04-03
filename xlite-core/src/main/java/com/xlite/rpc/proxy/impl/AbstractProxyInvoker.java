package com.xlite.rpc.proxy.impl;

import com.xlite.remoting.exchange.Response;
import com.xlite.rpc.*;

/**
 * <p>
 *
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/1
 **/
public abstract class AbstractProxyInvoker<T> implements Invoker<T> {

    private final T proxy;
    private final Class<T> type;

    public AbstractProxyInvoker(T proxy,Class<T> type){
        this.proxy = proxy;
        this.type = type;
    }

    @Override
    public Class<T> getInterface() {
        return type;
    }

    @Override
    public Response invoke(Invocation invocation) throws RpcException {
        try {
            return new RpcResponse(doInvoke(proxy,invocation.getMethodName(),invocation.getParameterTypes(),invocation.getArguments()));
        } catch (Throwable throwable) {
            throw new RpcException("Failed to invoke remote proxy method." + "cause: " + throwable.getMessage(),throwable);
        }
    }

    /**
     * 具体调用由子类实现【模板方法】
     * @param proxy
     * @param methodName
     * @param parameterTypes
     * @param arguments
     * @return
     */
    protected abstract Object doInvoke(T proxy,String methodName,Class<?>[] parameterTypes,Object[] arguments) throws Throwable;
}
