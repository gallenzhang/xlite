package com.xlite.rpc.proxy.impl;

import com.xlite.rpc.Invoker;
import com.xlite.rpc.ProxyFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * <p>
 *
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/1
 **/
public class JdkProxyFactory implements ProxyFactory {

    @Override
    public <T> T getProxy(Invoker<T> invoker) {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),new Class[]{invoker.getInterface()},new InvokerInvocationHandler(invoker));
    }

    @Override
    public <T> Invoker<T> getInvoker(T proxy, Class<T> type) {
        return new AbstractProxyInvoker<T>(proxy,type) {
            @Override
            protected Object doInvoke(T proxy, String methodName, Class<?>[] parameterTypes, Object[] arguments) throws Throwable {
                Method method = proxy.getClass().getMethod(methodName,parameterTypes);
                return method.invoke(proxy,arguments);
            }
        };
    }
}
