package com.xlite.remoting.exchange;

/**
 * <p>
 * 响应协议模型
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/1
 **/
public interface Response {

    /**
     * 请求正常响应后会返回一个Object
     * @return
     */
    Object getValue();

    /**
     * 请求发生异常
     * @return
     */
    Exception getException();


    /**
     * Request请求中的 requestId
     * @return
     */
    long getRequestId();

    /**
     * 处理时间
     * @return
     */
    long getProcessTime();

    /**
     * recreate
     * @return
     * @throws Throwable
     */
    Object recreate() throws Throwable;

    /**
     * 设置附加值
     * @param key
     * @param value
     */
    void setAttachment(String key,String value);

}
