package com.xlite.remoting.exchange;

/**
 * <p>
 * Callback
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/5
 **/
public interface ResponseCallback {

    /**
     * done
     * @param response
     */
    void done(Object response);

    /**
     * caught exception
     * @param exception
     */
    void caught(Throwable exception);
}
