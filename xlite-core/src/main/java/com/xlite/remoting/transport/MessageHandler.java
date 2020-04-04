package com.xlite.remoting.transport;

/**
 * <p>
 * 消息处理器
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/4
 **/
public interface MessageHandler {

    /**
     * 消息处理
     * @param channel
     * @param message
     * @return
     */
    Object handle(Channel channel, Object message);
}
