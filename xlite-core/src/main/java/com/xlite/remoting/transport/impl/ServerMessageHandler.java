package com.xlite.remoting.transport.impl;

import com.xlite.common.ReflectUtil;
import com.xlite.protocol.exporter.Exporter;
import com.xlite.remoting.exchange.Request;
import com.xlite.remoting.exchange.Response;
import com.xlite.remoting.transport.Channel;
import com.xlite.remoting.transport.MessageHandler;
import com.xlite.rpc.Invoker;
import com.xlite.rpc.RpcException;
import com.xlite.rpc.RpcInvocation;
import com.xlite.rpc.RpcResponse;

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
    protected Map<String,Exporter<?>> exporterMap;


    public ServerMessageHandler(Map<String,Exporter<?>> exporterMap){
        this.exporterMap = exporterMap;
    }

    @Override
    public Object handle(Channel channel, Object message) {
        if(channel == null || message == null){
            throw new RpcException("ServerMessageHandler handle(channel,message) param is null");
        }

        if(!(message instanceof Request)){
            throw new RpcException("ServerMessageHandler message type not support: " + message.getClass());
        }

        Request request = (Request) message;
        String serviceKey = request.getInterfaceName();

        Exporter exporter = exporterMap.get(serviceKey);
        if(exporter == null || exporter.getInvoker() == null){
            RpcException exception = new RpcException(this.getClass().getSimpleName() + " handler Error: provider not exist serviceKey=" + serviceKey + " " + request);

            RpcResponse response = new RpcResponse();
            response.setException(exception);
            return response;
        }

        try {
            Invoker invoker = exporter.getInvoker();
            Class<?>[] parametersType = ReflectUtil.forNames(request.getParametersDesc());
            RpcInvocation invocation = new RpcInvocation(request.getMethodName(),
                    parametersType,request.getArguments(), request.getAttachments());

            return invoker.invoke(invocation);
        } catch (ClassNotFoundException e) {
            RpcResponse response = new RpcResponse();
            response.setException(e);
            return response;
        } catch (Exception e){
            RpcResponse response = new RpcResponse();
            response.setException(e);
            return response;
        }
    }
}
