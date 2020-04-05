package com.xlite.remoting.transport.impl;

import com.xlite.remoting.NettyClient;
import com.xlite.remoting.NettyServer;
import com.xlite.remoting.transport.Client;
import com.xlite.remoting.transport.MessageHandler;
import com.xlite.remoting.transport.Server;
import com.xlite.remoting.transport.Transporter;

/**
 * <p>
 * netty transporter
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/4
 **/
public class NettyTransporter implements Transporter {


    @Override
    public Server createServer(MessageHandler messageHandler) {
        return new NettyServer(messageHandler);
    }

    @Override
    public Client createClient(String url) {
        return new NettyClient(url);
    }
}
