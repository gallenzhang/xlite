package com.xlite.protocol.exporter.impl;

import com.xlite.common.XliteFrameworkException;
import com.xlite.protocol.exporter.Exporter;
import com.xlite.rpc.Invoker;

/**
 * <p>
 * abstract exporter
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/4
 **/
public abstract class AbstractExporter<T> implements Exporter<T> {

    protected Invoker<T> invoker;

    protected volatile boolean init = false;
    protected volatile boolean available = false;


    @Override
    public void init() {
        //已经初始化过
        if(init){
            return;
        }

        boolean result = doInit();
        if(!result){
            throw new XliteFrameworkException(this.getClass().getSimpleName() + " node init Error. ",
                    XliteFrameworkException.Framework_INIT_ERROR);
        }else {
            init = true;
            available = true;
        }
    }

    /**
     * do init
     * @return
     */
    protected abstract boolean doInit();

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public void destroy() {

    }
}
