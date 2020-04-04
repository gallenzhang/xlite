package com.xlite.remoting.transport;

import com.xlite.remoting.exchange.Request;

/**
 * <p>
 * client
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/4
 **/
public interface Client extends Channel{

    /**
     * heart beat
     * @param request
     */
    void heartbeat(Request request);

}
