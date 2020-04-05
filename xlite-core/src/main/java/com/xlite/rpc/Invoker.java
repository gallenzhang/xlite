package com.xlite.rpc;

import com.xlite.remoting.exchange.Response;

/**
 * <p>
 * 方法调用实体域
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/1
 **/
public interface Invoker<T> extends Node{

    /**
     * 服务接口
     * @return
     */
    Class<T> getInterface();

    /**
     * 请求调用
     * @param invocation
     * @return
     */
    Response invoke(Invocation invocation) throws RpcException;
}
