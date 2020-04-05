package com.xlite.protocol.refer;

import com.xlite.common.ReflectUtil;
import com.xlite.remoting.exchange.Response;
import com.xlite.remoting.transport.Client;
import com.xlite.remoting.transport.impl.NettyTransporter;
import com.xlite.rpc.Invocation;
import com.xlite.rpc.Invoker;
import com.xlite.rpc.RpcException;
import com.xlite.rpc.RpcRequest;

/**
 * <p>
 * xlite invoker
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/5
 **/
public class XliteInvoker<T> implements Invoker<T> {

    private Class<T> type;

    private String url;

    private Client client;

    public XliteInvoker(Class<T> type,String url){
        this.type = type;
        this.url = url;
        client = new NettyTransporter().createClient(url);
    }


    @Override
    public Class getInterface() {
        return type;
    }

    @Override
    public Response invoke(Invocation invocation) throws RpcException {
        try {
            RpcRequest request = new RpcRequest();
            request.setInterfaceName(type.getName());
            request.setMethodName(invocation.getMethodName());
            request.setParametersDesc(ReflectUtil.getMethodParamDesc(invocation.getParameterTypes()));
            request.setArguments(invocation.getArguments());

            return client.request(request);
        }catch (Exception e){
            throw new RpcException(this.getClass().getSimpleName() + " invoke Error, url=" + url,e);
        }
    }

    @Override
    public void init() {
        client.open();
    }

    @Override
    public void destroy() {
        client.close();
    }

    @Override
    public boolean isAvailable() {
        return client.isAvailable();
    }
}
