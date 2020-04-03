package com.xlite.config;

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
    private T ref;

    /**
     * 点对点直连URL
     */
    private String url;

    public ReferenceConfig(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public Class<T> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public T get(){
        if(ref == null){
            initRef();
        }
        return ref;
    }

    /**
     * 实例化引用代理对象
     */
    private void initRef(){

    }
}
