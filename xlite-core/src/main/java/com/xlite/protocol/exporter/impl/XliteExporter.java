package com.xlite.protocol.exporter.impl;

import com.xlite.protocol.exporter.Exporter;
import com.xlite.remoting.transport.Server;
import com.xlite.remoting.transport.impl.NettyTransporter;
import com.xlite.remoting.transport.impl.ServerMessageHandler;
import com.xlite.rpc.Invoker;

import java.util.Map;

/**
 * <p>
 * xlite exporter
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/1
 **/
public class XliteExporter<T> extends AbstractExporter {

    private Server server;

    /**
     * exporter map
     */
    protected Map<String,Exporter<?>> exporterMap = null;

    public XliteExporter(Invoker<T> invoker, Map<String,Exporter<?>> exporterMap){
        this.invoker = invoker;
        this.exporterMap = exporterMap;
        server = new NettyTransporter().createServer(new ServerMessageHandler(exporterMap));
    }

    @Override
    public Invoker getInvoker() {
        return this.invoker;
    }

    @Override
    public void unexport() {
        exporterMap.remove(invoker.getInterface().getName());
    }

    @Override
    protected boolean doInit() {
        return server.open();
    }
}
