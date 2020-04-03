package com.xlite.protocol.xlite;

import com.xlite.rpc.Exporter;
import com.xlite.rpc.Invoker;

import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/1
 **/
public class XliteExporter<T> implements Exporter {

    private Invoker<T> invoker;
    private String key;
    private Map<String,Exporter<?>> exporterMap;

    public XliteExporter(Invoker<T> invoker,String key,Map<String,Exporter<?>> exporterMap){
        this.invoker = invoker;
        this.key = key;
        this.exporterMap = exporterMap;
    }

    @Override
    public Invoker getInvoker() {
        return this.invoker;
    }

    @Override
    public void unexport() {
        exporterMap.remove(key);
    }
}
