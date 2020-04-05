package com.xlite.config;

import com.xlite.protocol.XliteProtocol;
import com.xlite.rpc.Invoker;
import com.xlite.rpc.Protocol;
import com.xlite.rpc.proxy.ProxyFactory;
import com.xlite.rpc.proxy.impl.JdkProxyFactory;

/**
 * <p>
 * 服务消费者配置
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/1
 **/
public class ReferenceConfig<T> {

    /**
     * 接口
     */
    private Class<T> interfaceClass;

    /**
     * 接口实现代理
     */
    private transient volatile T ref;

    /**
     * 初始化标识
     */
    private transient volatile boolean initialized;

    /**
     * xlite protocol
     */
    private static final Protocol refProtocol = new XliteProtocol();

    /**
     * 代理工厂
     */
    private static final ProxyFactory proxyFactory = new JdkProxyFactory();

    /**
     * 点对点直连URL
     */
    private String directUrl;

    public ReferenceConfig(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public Class<T> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public String getDirectUrl() {
        return directUrl;
    }

    public void setDirectUrl(String directUrl) {
        this.directUrl = directUrl;
    }

    public synchronized T get(){
        if(ref == null){
            initRef();
        }
        return ref;
    }

    /**
     * 实例化引用代理对象
     */
    private void initRef(){
        if(initialized){
            return;
        }

        initialized = true;

        ref = createProxy();
    }

    /**
     * 生成动态代理
     * @return
     */
    private T createProxy() {
        Invoker<T> invoker = refProtocol.refer(interfaceClass,directUrl);
        invoker.init();

        return proxyFactory.getProxy(invoker);
    }


}
