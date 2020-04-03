package com.xlite.rpc.proxy.impl;

import com.xlite.rpc.Invoker;
import com.xlite.rpc.RpcInvocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * <p>
 *
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/1
 **/
public class InvokerInvocationHandler implements InvocationHandler {

    private Invoker<?> invoker;

    public InvokerInvocationHandler(Invoker<?> handler){
        this.invoker = handler;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        if("toString".equals(methodName) && method.getParameterTypes().length == 0){
            return invoker.toString();
        }

        if("hashCode".equals(methodName) && method.getParameterTypes().length == 0){
            return invoker.hashCode();
        }

        if("equals".equals(methodName) && method.getParameterTypes().length == 1){
            return invoker.equals(args[0]);
        }
        return invoker.invoke(new RpcInvocation(method,args)).recreate();
    }
}
