package com.xlite.rpc.proxy.impl;

import com.xlite.remoting.exchange.Response;
import com.xlite.rpc.Invocation;
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

        //远程调用
        Invocation invocation = new RpcInvocation(method,args);
        try {
            Response response = invoker.invoke(invocation);

            if(response.getException() == null){
                return response.getValue();
            }else {
                throw new Exception(response.getException());
            }

        }catch (Exception e){
            throw e;
        }
    }

}
