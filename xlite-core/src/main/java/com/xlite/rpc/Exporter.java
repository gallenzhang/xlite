package com.xlite.rpc;

/**
 * <p>
 *
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
}
