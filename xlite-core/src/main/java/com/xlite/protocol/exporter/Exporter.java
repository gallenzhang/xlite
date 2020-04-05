package com.xlite.protocol.exporter;

import com.xlite.rpc.Invoker;
import com.xlite.rpc.Node;

/**
 * <p>
 * exporter
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/1
 **/
public interface Exporter<T> extends Node {

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
