package com.xlite.rpc;

/**
 * <p>
 * invoke timeout exception
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/5
 **/
public class TimeoutException extends Exception{

    public TimeoutException(String message) {
        super(message);
    }
}
