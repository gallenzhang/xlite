package com.xlite.remoting.transport;

/**
 * <p>
 * transporter
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/4
 **/
public interface Transporter {

    /**
     * create server
     * @param messageHandler
     * @return
     */
    Server createServer(MessageHandler messageHandler);


    /**
     * create remote client
     * @return
     */
    Client createClient(String url);

}
