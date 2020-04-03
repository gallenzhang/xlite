package com.xlite.remoting.exchange;

import java.util.Map;

/**
 * <p>
 * 请求协议模型
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/1
 **/
public interface Request {

    /**
     * 接口名
     * @return
     */
    String getInterfaceName();

    /**
     * 方法名
     * @return
     */
    String getMethodName();

    /**
     * 方法参数签名
     * @return
     */
    String getParametersDesc();

    /**
     * 方法调用参数
     * @return
     */
    Object[] getArguments();

    /**
     * 附加参数
     * @return
     */
    Map<String,String> getAttachments();

    /**
     * 设置附加参数
     * @param key
     * @param value
     */
    void setAttachment(String key,String value);

    /**
     * 获取附加参数
     * @param key
     */
    String getAttachment(String key);

    /**
     * 带默认值的获取附加参数
     * @param key
     * @param defaultValue
     */
    String getAttachment(String key, String defaultValue);

    /**
     * 请求ID
     * @return
     */
    long getRequestId();

}
