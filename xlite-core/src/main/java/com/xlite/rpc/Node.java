package com.xlite.rpc;

/**
 * <p>
 * node
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/5
 **/
public interface Node {

    /**
     * init
     */
    void init();

    /**
     * destroy
     */
    void destroy();

    /**
     * is available
     * @return
     */
    boolean isAvailable();
}
