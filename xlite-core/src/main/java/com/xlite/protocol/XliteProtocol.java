package com.xlite.protocol;

import com.xlite.common.NetUtils;
import com.xlite.protocol.xlite.Constants;
import com.xlite.protocol.xlite.XliteExporter;
import com.xlite.remoting.NettyServer;
import com.xlite.rpc.Exporter;
import com.xlite.rpc.Invoker;
import com.xlite.rpc.Protocol;
import com.xlite.rpc.RpcException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 *
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/1
 **/
public class XliteProtocol implements Protocol {

    private Map<String,Exporter<?>> exporterMap = new ConcurrentHashMap<>();
    private Map<String,NettyServer> serverMap = new ConcurrentHashMap<>();

    @Override
    public <T> Exporter<T> export(Invoker<T> invoker) throws RpcException {
        String key = invoker.getInterface().getName();
        XliteExporter<T> exporter = new XliteExporter<>(invoker,key,exporterMap);
        exporterMap.put(key,exporter);

        //开启服务器
        openServer();
        return exporter;
    }

    /**
     * 开启服务端
     */
    private void openServer(){
        String key = NetUtils.getIP() + ":" + Constants.DEFAULT_PORT;
        if(serverMap.get(key) == null){
            NettyServer server = new NettyServer();
            serverMap.put(key,server);
        }
    }

    @Override
    public <T> Invoker<T> refer(Class<T> type) {
        return null;
    }
}
