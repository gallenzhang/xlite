package com.xlite.remoting.transport;

import com.xlite.remoting.exchange.Request;
import com.xlite.remoting.exchange.Response;

import java.net.InetSocketAddress;

/**
 * <p>
 * channel
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/4
 **/
public interface Channel {

    /**
     * get local address
     * @return
     */
    InetSocketAddress getLocalAddress();

    /**
     * get remote address
     * @return
     */
    InetSocketAddress getRemoteAddress();

    /**
     * request
     * @param request
     * @return
     */
    Response request(Request request);

    /**
     * open the channel
     * @return
     */
    boolean open();

    /**
     * close the channel
     */
    void close();

    /**
     * is closed
     * @return
     */
    boolean isClosed();

    /**
     * available status
     * @return
     */
    boolean isAvailable();

}
