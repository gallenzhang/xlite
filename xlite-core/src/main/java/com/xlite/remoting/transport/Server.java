package com.xlite.remoting.transport;

import java.net.InetSocketAddress;
import java.util.Collection;

/**
 * <p>
 * server
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/4
 **/
public interface Server extends Channel{


    /**
     * get all channels
     * @return
     */
    Collection<Channel> getAllChannels();


    /**
     * get channel by remote address
     * @param remoteAddress
     * @return
     */
    Channel getChannel(InetSocketAddress remoteAddress);

}
