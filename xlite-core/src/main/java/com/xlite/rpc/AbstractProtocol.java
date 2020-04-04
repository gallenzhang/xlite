package com.xlite.rpc;

import com.xlite.common.XliteFrameworkException;
import com.xlite.protocol.exporter.Exporter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 抽象协议类
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/4
 **/
public abstract class AbstractProtocol implements Protocol{

    /**
     * exporter map
     */
    protected Map<String,Exporter<?>> exporterMap = new ConcurrentHashMap<>();


    @Override
    public <T> Exporter<T> export(Invoker<T> invoker) throws RpcException {
        String exporterKey = invoker.getInterface().getName();

        synchronized (exporterMap){
            if(exporterMap.get(exporterKey) != null){
                throw new XliteFrameworkException(invoker.getInterface().getName() + " export Error: service "
                + "already exist. ",XliteFrameworkException.Framework_INIT_ERROR);
            }


            Exporter<T> exporter = createExporter(invoker);
            exporter.init();

            exporterMap.put(exporterKey,exporter);
            return exporter;
        }
    }

    @Override
    public <T> Invoker<T> refer(Class<T> type) {
        return null;
    }

    /**
     * 创建exporter
     * @param invoker
     * @param <T>
     * @return
     */
    public abstract <T> Exporter<T> createExporter(Invoker<T> invoker);


    /**
     * 创建invoker
     * @param type
     * @param <T>
     * @return
     */
    public abstract <T> Invoker<T> createRefer(Class<T> type);

}
