package com.xlite.codec.impl;

import com.xlite.codec.Codec;
import com.xlite.common.ByteUtils;
import com.xlite.remoting.exchange.Request;
import com.xlite.remoting.exchange.Response;
import com.xlite.rpc.RpcException;
import com.xlite.rpc.RpcRequest;
import com.xlite.rpc.RpcResponse;
import com.xlite.serialization.Serialization;
import com.xlite.serialization.impl.Hessian2Serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Map;

/**
 * <p>
 * 默认编解码实现
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/2
 **/
public class DefaultRpcCodec extends AbstractRpcCodec implements Codec {

    /**
     * 请求头长度
     */
    public static final int HEADER_LENGTH = 16;

    /**
     * 魔数
     */
    public static final short MAGIC = (short) 0xbaba;

    /**
     * request flag
     */
    public final static byte FLAG_REQUEST = 0x00;

    /**
     * response flag
     */
    public final static byte FLAG_RESPONSE = (byte) 0x80;

    @Override
    public byte[] encode(Object msg) throws IOException {
        try {
            if(msg instanceof Request){
                return encodeRequest((Request) msg);
            }else if(msg instanceof Response){
                return encodeResponse((Response) msg);
            }
        }catch (Exception e){
            throw new RpcException("encode error: isResponse=" + (msg instanceof Response),e,RpcException.ENCODE_EXCEPTION);
        }

        throw new RpcException("encode error: message type not support, " + msg.getClass(),RpcException.ENCODE_EXCEPTION);
    }

    @Override
    public Object decode(byte[] bytes) throws IOException {
        if(bytes.length <= HEADER_LENGTH){
            throw new RpcException("decode error: format error",RpcException.DECODE_EXCEPTION);
        }

        int offset = 0;

        //魔数校验
        if(ByteUtils.bytes2short(bytes,offset) != MAGIC){
            throw new RpcException("decode error: magic error",RpcException.DECODE_EXCEPTION);
        }

        //协议版本号校验
        if((byte) 1 != bytes[offset += 2]){
            throw new RpcException("decode error: version error",RpcException.DECODE_EXCEPTION);
        }

        byte flag = bytes[++offset];
        boolean isResponse = (flag & FLAG_RESPONSE) == FLAG_RESPONSE;

        //request id
        long requestId = ByteUtils.bytes2long(bytes,++offset);

        //body length
        int bodyLength = ByteUtils.bytes2int(bytes,12);

        byte[] body = new byte[bodyLength];
        System.arraycopy(bytes,HEADER_LENGTH,body,0,bodyLength);

        Serialization serialization = new Hessian2Serialization();
        try {
            if(isResponse){
                return decodeResponse(body,requestId,serialization);
            }else {
                return decodeRequest(body,requestId,serialization);
            }
        }catch (Exception e){
            throw new RpcException("encode error: isResponse=" + isResponse,e,RpcException.ENCODE_EXCEPTION);
        }
    }

    /**
     * encode request
     * @param request
     * @return
     */
    private byte[] encodeRequest(Request request) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput output = new ObjectOutputStream(bos);
        output.writeUTF(request.getInterfaceName());
        output.writeUTF(request.getMethodName());
        output.writeUTF(request.getParametersDesc());

        Serialization serialization = new Hessian2Serialization();
        if(request.getArguments() != null && request.getArguments().length > 0){
            for (Object object : request.getArguments()){
                serialize(output,serialization,object);
            }
        }

        if(request.getAttachments() == null || request.getAttachments().size() == 0){
            output.writeInt(0);
        }else {
            output.writeInt(request.getAttachments().size());
            for(Map.Entry<String,String> entry : request.getAttachments().entrySet()){
                output.writeUTF(entry.getKey());
                output.writeUTF(entry.getValue());
            }
        }
        output.flush();

        //请求报文体
        byte[] requestBody = bos.toByteArray();

        //请求报文头
        byte[] requestHeader = getRequestHeader(request.getRequestId(),serialization,requestBody.length,true);

        //请求报文 = 报文头 + 报文体
        byte[] requestData = new byte[requestHeader.length + requestBody.length];
        System.arraycopy(requestHeader,0,requestData,0,requestHeader.length);
        System.arraycopy(requestBody,0,requestData,requestHeader.length,requestBody.length);
        return requestBody;
    }

    /**
     * 组装报文头
     * @param requestId
     * @param serialization
     * @param bodyLength
     * @return
     */
    private byte[] getRequestHeader(long requestId,Serialization serialization,int bodyLength,boolean isRequest){
        byte[] header = new byte[HEADER_LENGTH];

        int offset = 0;

        //写入魔数(0-15 bit)
        ByteUtils.short2bytes(MAGIC,header);
        offset += 2;

        //写入版本号(16-23 bit)
        header[offset++] = (byte) 1;

        //写入扩展标记位(24-31 bit)
        if(isRequest){
            header[offset++] = (byte) (FLAG_REQUEST | serialization.getSerializationId());
        }else {
            header[offset++] = (byte) (FLAG_RESPONSE | serialization.getSerializationId());
        }

        //写入request id(32-95 bit)
        ByteUtils.long2bytes(requestId,header,offset);
        offset += 8;

        //写入body length(96-127 bit)
        ByteUtils.int2bytes(bodyLength,header,offset);
        return header;
    }


    /**
     * encode response
     * @param response
     * @return
     */
    private byte[] encodeResponse(Response response) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput output = new ObjectOutputStream(bos);

        Serialization serialization = new Hessian2Serialization();

        //方法调用处理耗时
        output.writeLong(response.getProcessTime());

        //响应结果类型
        output.writeUTF(response.getValue().getClass().getName());
        serialize(output,serialization,response.getValue());
        output.flush();

        //请求报文体
        byte[] body = bos.toByteArray();

        //请求报文头
        byte[] header = getRequestHeader(response.getRequestId(),serialization,body.length,false);

        //请求报文 = 报文头 + 报文体
        byte[] data = new byte[header.length + body.length];
        System.arraycopy(header,0,data,0,header.length);
        System.arraycopy(body,0,data,header.length,body.length);
        return data;
    }

    /**
     * 解码请求
     * @param bytes
     * @param requestId
     * @param serialization
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private Request decodeRequest(byte[] bytes, long requestId,Serialization serialization) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput input = new ObjectInputStream(bis);

        RpcRequest request = new RpcRequest();
        request.setRequestId(requestId);

        //接口名称、方法名、方法参数签名
        String interfaceName = input.readUTF();
        String methodName = input.readUTF();
        String parametersDesc = input.readUTF();

        request.setInterfaceName(interfaceName);
        request.setMethodName(methodName);
        request.setParametersDesc(parametersDesc);
        request.setArguments(decodeArgument(input,parametersDesc,serialization));
        request.setAttachments(decodeAttachment(input));

        input.close();
        return request;
    }

    /**
     * 解码响应
     * @param bytes
     * @param requestId
     * @param serialization
     * @return
     */
    private Response decodeResponse(byte[] bytes,long requestId,Serialization serialization){
        RpcResponse response = new RpcResponse(null);
        return response;
    }
}
