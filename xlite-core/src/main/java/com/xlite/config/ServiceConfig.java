package com.xlite.config;

import com.xlite.protocol.XliteProtocol;
import com.xlite.protocol.exporter.Exporter;
import com.xlite.rpc.Invoker;
import com.xlite.rpc.Protocol;
import com.xlite.rpc.proxy.ProxyFactory;
import com.xlite.rpc.proxy.impl.JdkProxyFactory;


import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务提供者配置
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/1
 **/
public class ServiceConfig<T> {

    /**
     * 接口类
     */
    private Class<T> interfaceClass;

    private ProxyFactory proxyFactory = new JdkProxyFactory();
    private Protocol protocol = new XliteProtocol();
    private List<Exporter<?>> exporters = new ArrayList<>();
    /**
     * 接口实现
     */
    private T instance;

    public ServiceConfig(Class<T> interfaceClass, T instance) {
        this.interfaceClass = interfaceClass;
        this.instance = instance;
    }

    public Class<T> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public T getInstance() {
        return instance;
    }

    public void setInstance(T instance) {
        this.instance = instance;
    }

    /**
     * 服务导出
     */
    public void export(){
        Invoker<?> invoker = proxyFactory.getInvoker(instance,interfaceClass);
        Exporter<?> exporter = protocol.export(invoker);
        exporters.add(exporter);
    }
}
