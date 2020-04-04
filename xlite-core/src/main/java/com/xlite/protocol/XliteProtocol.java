package com.xlite.protocol;

import com.xlite.protocol.exporter.Exporter;
import com.xlite.protocol.exporter.impl.XliteExporter;
import com.xlite.rpc.AbstractProtocol;
import com.xlite.rpc.Invoker;

/**
 * <p>
 * xlite协议实现
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/1
 **/
public class XliteProtocol extends AbstractProtocol {


    @Override
    public <T> Exporter<T> createExporter(Invoker<T> invoker) {
        return new XliteExporter<T>(invoker,exporterMap);
    }

    @Override
    public <T> Invoker<T> createRefer(Class<T> type) {
        return null;
    }
}
