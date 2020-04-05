package com.xlite.rpc;

/**
 * <p>
 * Rpc调用异常
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/1
 **/
public final class RpcException extends RuntimeException{

    /**
     * 未知异常
     */
    public static final int UNKNOWN_EXCEPTION = 0;

    /**
     * 网络异常
     */
    public static final int NETWORK_EXCEPTION = 1;

    /**
     * 序列化异常
     */
    public static final int SERIALIZATION_EXCEPTION = 2;

    /**
     * 编码异常
     */
    public static final int ENCODE_EXCEPTION = 3;

    /**
     * 解码异常
     */
    public static final int DECODE_EXCEPTION = 4;

    /**
     * 业务异常
     */
    public static final int BIZ_EXCEPTION = 5;

    /**
     * 服务拒绝异常
     */
    public static final int SERVER_REJECT = 6;


    /**
     * 异常码
     */
    private int code;

    public RpcException() {
        super();
    }

    public RpcException(int code) {
        super();
        this.code = code;
    }

    public RpcException(String message) {
        super(message);
    }

    public RpcException(String message,Throwable cause) {
        super(message,cause);
    }

    public RpcException(String message,int code) {
        super(message);
        this.code = code;
    }

    public RpcException(String message,Throwable cause,int code) {
        super(message,cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
