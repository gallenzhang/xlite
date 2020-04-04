package com.xlite.common;

/**
 * <p>
 * Xlite常量类
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/3
 **/
public class XliteConstant {

    /**
     * 默认绑定端口
     */
    public static final int DEFAULT_PORT = 20660;

    /**
     * 版本
     */
    public final static byte VERSION = 0x01;

    /**
     * 响应异常
     */
    public final static int RESPONSE_EXCEPTION = 1;

    /**
     * 返回为空
     */
    public final static int RESPONSE_VOID = 2;

    /**
     * 正常返回值
     */
    public final static int RESPONSE_NORMAL = 3;


    /**
     * 最小工作线程
     */
    public final static int MIN_WORKER_THREAD = 20;

    /**
     * 最大工作线程
     */
    public final static int MAX_WORKER_THREAD = 20;

    /**
     * 队列大小
     */
    public final static int WORKER_QUEUE_SIZE = 100;

    /**
     * 服务端支持的最大连接数
     */
    public final static int MAX_SERVER_CONNECTION = 100000;


    /**
     * max idle time
     */
    public static final int DEFAULT_MAX_IDLE_TIME = 60 * 1000;

}
