package com.xlite.remoting.transport;

import com.xlite.common.ChannelState;

import java.net.InetSocketAddress;
import java.util.Collection;

/**
 * <p>
 * abstract server
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/4
 **/
public abstract class AbstractServer implements Server{

    protected InetSocketAddress local;

    protected InetSocketAddress remote;

    protected volatile ChannelState state = ChannelState.UN_INIT;

    @Override
    public Collection<Channel> getAllChannels() {
        return null;
    }

    @Override
    public Channel getChannel(InetSocketAddress remoteAddress) {
        return null;
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return local;
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return remote;
    }
}
