package com.xlite.protocol.exporter;

import com.xlite.rpc.Invoker;

/**
 * <p>
 * exporter
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/1
 **/
public interface Exporter<T> {

    /**
     * get invoker
     * @return
     */
    Invoker<T> getInvoker();

    /**
     * unexport
     */
    void unexport();

    /**
     * init
     */
    void init();

    /**
     * is available
     * @return
     */
    boolean isAvailable();
}
