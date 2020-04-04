package com.xlite.rpc;

import com.xlite.remoting.exchange.Response;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * RpcResponse
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/2
 **/
public class RpcResponse implements Response, Serializable {


    /**
     * 请求响应value
     */
    private Object value;

    /**
     * 请求Exception
     */
    private Exception exception;

    /**
     * requestId
     */
    private long requestId;

    /**
     * 响应时间
     */
    private long processTime;

    /**
     * 附加参数
     */
    private Map<String, String> attachments;

    public RpcResponse(){

    }

    public RpcResponse(Object value){
        this.value = value;
    }

    public RpcResponse(Object value,long requestId){
        this.value = value;
        this.requestId = requestId;
    }

    public boolean isOk(){
        return exception == null;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public Exception getException() {
        return exception;
    }

    @Override
    public long getRequestId() {
        return requestId;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    @Override
    public long getProcessTime() {
        return processTime;
    }

    @Override
    public Object recreate() throws Throwable{
        if(exception == null){
            return value;
        }
        throw exception;
    }

    @Override
    public void setAttachment(String key, String value) {
        if(attachments == null){
            attachments = new HashMap<>();
        }

        this.attachments.put(key,value);
    }

    public void setProcessTime(long processTime) {
        this.processTime = processTime;
    }

    @Override
    public String toString() {
        return "RpcResponse{" +
                "value=" + value +
                ", exception=" + exception +
                ", requestId=" + requestId +
                ", processTime=" + processTime +
                '}';
    }
}
