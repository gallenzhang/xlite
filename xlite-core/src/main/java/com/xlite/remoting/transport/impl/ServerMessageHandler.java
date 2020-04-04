package com.xlite.remoting.transport.impl;

import com.xlite.protocol.exporter.Exporter;
import com.xlite.remoting.transport.Channel;
import com.xlite.remoting.transport.MessageHandler;

import java.util.Map;

/**
 * <p>
 * server message handler
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/4
 **/
public class ServerMessageHandler implements MessageHandler {

    /**
     * exporter map
     */
    protected Map<String,Exporter<?>> exporterMap = null;


    public ServerMessageHandler(Map<String,Exporter<?>> exporterMap){
        this.exporterMap = exporterMap;
    }

    @Override
    public Object handle(Channel channel, Object message) {
        return null;
    }
}
