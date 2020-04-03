package com.xlite.rpc;

import java.util.Map;

/**
 * <p>
 * 调用参数封装类
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/1
 **/
public interface Invocation {


    /**
     * 方法名
     * @return
     */
    String getMethodName();

    /**
     * 方法参数
     * @return
     */
    Class<?>[] getParameterTypes();

    /**
     * 方法参数
     * @return
     */
    Object[] getArguments();

    /**
     * 附加参数
     * @return
     */
    Map<String,String> getAttachments();

    /**
     * 根据key获取附加参数
     * @param key
     * @return
     */
    String getAttachment(String key);

    /**
     * 带有默认值的获取附加参数
     * @param key
     * @param defaultValue
     * @return
     */
    String getAttachment(String key, String defaultValue);
}
