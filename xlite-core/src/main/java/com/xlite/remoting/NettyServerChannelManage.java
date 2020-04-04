package com.xlite.remoting;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 服务端channel连接管理
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/4
 **/
@ChannelHandler.Sharable
public class NettyServerChannelManage extends ChannelInboundHandlerAdapter {

    /**
     * 维持客户端与服务端的连接
     */
    private Map<String,Channel> channels = new ConcurrentHashMap<>();

    /**
     * 最大连接数
     */
    private int maxChannel;

    public NettyServerChannelManage(int maxChannel){
        super();
        this.maxChannel = maxChannel;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        if(channels.size() >= maxChannel){
            channel.close();
        }else {
            String key = getKey((InetSocketAddress) channel.localAddress(), (InetSocketAddress) channel.remoteAddress());
            channels.put(key,channel);
        }
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channels.remove(getKey((InetSocketAddress) channel.localAddress(), (InetSocketAddress) channel.remoteAddress()));
        super.channelUnregistered(ctx);
    }

    /**
     * remote address - local address 作为一条连接的唯一标识
     * @param local
     * @param remote
     * @return
     */
    private String getKey(InetSocketAddress local,InetSocketAddress remote){
        String key = "";
        if(local == null || local.getAddress() == null){
            key += "null-";
        }else {
            key += local.getAddress().getHostAddress() + ":" + local.getPort() + "-";
        }

        if(remote == null || remote.getAddress() == null){
            key += "null";
        }else {
            key += remote.getAddress().getHostAddress() + ":" + remote.getPort();
        }
        return key;
    }

    /**
     * return channels
     * @return
     */
    public Map<String,Channel> getChannels(){
        return channels;
    }


    /**
     * close all channels
     */
    public void close(){
        for (Map.Entry<String,Channel> entry : channels.entrySet()){
            try {
                if(entry.getValue() != null){
                    entry.getValue().close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
