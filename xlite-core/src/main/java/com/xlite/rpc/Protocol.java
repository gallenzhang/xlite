package com.xlite.rpc;

import com.xlite.protocol.exporter.Exporter;

/**
 * <p>
 *
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/1
 **/
public interface Protocol {

    /**
     * 服务导出
     * @param invoker
     * @param <T>
     * @return
     * @throws RpcException
     */
    <T> Exporter<T> export(Invoker<T> invoker) throws RpcException;

    /**
     * 服务引入
     * @param type
     * @param <T>
     * @return
     */
    <T> Invoker<T> refer(Class<T> type);
}
