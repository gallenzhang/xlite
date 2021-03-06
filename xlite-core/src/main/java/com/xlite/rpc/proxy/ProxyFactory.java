package com.xlite.rpc.proxy;

import com.xlite.rpc.Invoker;
import com.xlite.rpc.RpcException;

/**
 * <p>
 * 代理工厂接口
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/1
 **/
public interface ProxyFactory {

    /**
     * 创建代理
     * @param invoker
     * @param <T>
     * @return
     */
    <T> T getProxy(Invoker<T> invoker);

    /**
     * 创建Invoker
     * @param proxy
     * @param type
     * @param <T>
     * @return
     */
    <T> Invoker<T> getInvoker(T proxy, Class<T> type) throws RpcException;
}
