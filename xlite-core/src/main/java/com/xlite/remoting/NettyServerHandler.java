package com.xlite.remoting;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 *
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/1
 **/
@io.netty.channel.ChannelHandler.Sharable
public class NettyServerHandler extends ChannelDuplexHandler {

    /**
     * <ip:port,channel>
     */
    private final Map<String,Channel> channels = new ConcurrentHashMap<>();


}
