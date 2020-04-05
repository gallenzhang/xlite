package com.xlite.remoting.exchange;

import com.xlite.rpc.TimeoutException;

/**
 * <p>
 * Future
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/5
 **/
public interface ResponseFuture {

    /**
     * get result
     * @return
     */
    Object get() throws TimeoutException;

    /**
     * get result with the timeout(ms)
     * @param timeout
     * @return
     */
    Object get(int timeout) throws TimeoutException;

    /**
     * set callback
     * @param callback
     */
    void setCallback(ResponseCallback callback);

    /**
     * check is done
     * @return
     */
    boolean isDone();
}
