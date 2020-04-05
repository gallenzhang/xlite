package com.xlite.remoting;

import com.xlite.codec.Codec;
import com.xlite.codec.impl.DefaultRpcCodec;
import com.xlite.common.CodecUtils;
import com.xlite.common.NetUtils;
import com.xlite.common.XliteConstant;
import com.xlite.common.XliteFrameworkException;
import com.xlite.remoting.exchange.Request;
import com.xlite.remoting.exchange.Response;
import com.xlite.remoting.transport.Channel;
import com.xlite.remoting.transport.MessageHandler;
import com.xlite.rpc.RpcException;
import com.xlite.rpc.RpcResponse;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p>
 * 业务处理
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/1
 **/
@io.netty.channel.ChannelHandler.Sharable
public class NettyChannelHandler extends ChannelDuplexHandler {

    private static final Logger logger = LoggerFactory.getLogger(NettyChannelHandler.class);

    private ThreadPoolExecutor threadPoolExecutor;

    private MessageHandler messageHandler;

    private Channel channel;

    private Codec codec = new DefaultRpcCodec();

    public NettyChannelHandler(Channel channel,MessageHandler messageHandler,ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
        this.messageHandler = messageHandler;
        this.channel = channel;
    }

    public NettyChannelHandler(Channel channel, MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
        this.channel = channel;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if(threadPoolExecutor == null){
            processMessage(ctx,msg);
        }else {
            try {
                threadPoolExecutor.execute(()->processMessage(ctx,channel));

            }catch (RejectedExecutionException rejectException){
                if(msg instanceof Request){
                    //如果是请求，则直接拒绝
                    rejectMessage(ctx,msg);

                }else if (msg instanceof Response){
                    //如果是响应，则继续处理
                    logger.warn("process thread pool is full, run in to thread.");
                    processMessage(ctx,msg);
                }
            }
        }
    }

    /**
     * 响应请求-处理线程池拒绝
     * @param ctx
     * @param msg
     */
    private void rejectMessage(ChannelHandlerContext ctx, Object msg) {
        if(msg instanceof Request){
            RpcResponse response = new RpcResponse();
            response.setRequestId(((Request) msg).getRequestId());
            response.setException(new RpcException("process thread poll is full, reject by server: " + ctx.channel().localAddress()
                    ,RpcException.SERVER_REJECT));

            sendResponse(ctx,response);
        }
    }

    /**
     * 消息处理
     * @param ctx
     * @param msg
     */
    private void processMessage(ChannelHandlerContext ctx, Object msg){
        if(msg instanceof Request){
            processRequest(ctx, (Request) msg);
        }else if(msg instanceof Response){
            processResponse(msg);
        }else {
            throw new XliteFrameworkException("NettyServerHandler message received type not support=" + msg.getClass());
        }
    }

    /**
     * 处理请求
     * @param ctx
     * @param request
     */
    private void processRequest(ChannelHandlerContext ctx,Request request){
        request.setAttachment(XliteConstant.HOST,NetUtils.getHostAddress(ctx.channel().remoteAddress()));
        long startTime = System.currentTimeMillis();

        Object result = messageHandler.handle(channel,request);
        RpcResponse response;
        if(result instanceof RpcResponse){
            response = (RpcResponse) result;
        }else {
            response = new RpcResponse(result);
        }

        response.setRequestId(request.getRequestId());
        response.setProcessTime(System.currentTimeMillis() - startTime);
        sendResponse(ctx,response);
    }

    /**
     * 发送响应
     * @param ctx
     * @param response
     */
    private ChannelFuture sendResponse(ChannelHandlerContext ctx, RpcResponse response) {
        byte[] msg = CodecUtils.encodeToBytes(codec,response);
        response.setAttachment(XliteConstant.CONTENT_LENGTH,String.valueOf(msg.length));
        if(ctx.channel().isActive()){
            return ctx.channel().writeAndFlush(msg);
        }
        return null;
    }


    /**
     * 处理响应
     * @param msg
     */
    private void processResponse(Object msg){
        messageHandler.handle(channel,msg);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
    }
}
