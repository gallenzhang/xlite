package com.xlite.common;

/**
 * <p>
 * xilite框架异常
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/4
 **/
public class XliteFrameworkException extends RuntimeException {

    /**
     * 初始化异常
     */
    public static final int Framework_INIT_ERROR = 1;


    /**
     * 异常码
     */
    private int code;

    public XliteFrameworkException() {
        super();
    }

    public XliteFrameworkException(int code) {
        super();
        this.code = code;
    }

    public XliteFrameworkException(String message) {
        super(message);
    }

    public XliteFrameworkException(String message,Throwable cause) {
        super(message,cause);
    }

    public XliteFrameworkException(String message,int code) {
        super(message);
        this.code = code;
    }

    public XliteFrameworkException(String message,Throwable cause,int code) {
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
