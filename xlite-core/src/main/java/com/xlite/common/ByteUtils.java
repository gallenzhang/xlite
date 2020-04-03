package com.xlite.common;

/**
 * <p>
 * 字节数组工具类
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/3
 **/
public class ByteUtils {

    /**
     * 将short类型value转为2个字节，高位在前
     * @param value
     * @param bytes
     */
    public static void short2bytes(short value,byte[] bytes){
        short2bytes(value,bytes,0);
    }

    /**
     * 将short类型value转为2个字节，高位在前
     * @param value
     * @param bytes
     * @param offset
     */
    public static void short2bytes(short value,byte[] bytes,int offset){
        bytes[offset+1] = (byte) value;
        bytes[offset] = (byte) (value >>> 8);
    }

    /**
     * long类型value转为8个字节，高位在前
     * @param value
     * @param bytes
     * @param offset
     */
    public static void long2bytes(long value,byte[] bytes,int offset){
        bytes[offset + 7] = (byte) value;
        bytes[offset + 6] = (byte) (value >>> 8);
        bytes[offset + 5] = (byte) (value >>> 16);
        bytes[offset + 4] = (byte) (value >>> 24);
        bytes[offset + 3] = (byte) (value >>> 32);
        bytes[offset + 2] = (byte) (value >>> 40);
        bytes[offset + 1] = (byte) (value >>> 48);
        bytes[offset] = (byte) (value >>> 56);
    }

    /**
     * int类型value转为4个字节，高位在前
     * @param value
     * @param bytes
     * @param offset
     */
    public static void int2bytes(int value,byte[] bytes,int offset){
        bytes[offset + 3] = (byte) value;
        bytes[offset + 2] = (byte) (value >>> 8);
        bytes[offset + 1] = (byte) (value >>> 16);
        bytes[offset] = (byte) (value >>> 24);
    }

    /**
     * 把字节数组中offset开始的2个字节，转为short类型
     * @param bytes
     * @param offset
     * @return
     */
    public static short bytes2short(byte[] bytes, int offset){
        return (short) ((bytes[offset + 1] & 0xFF) + ((bytes[offset] & 0xFF) << 8));
    }

    /**
     * 把字节数组中offset开始的前4个字节，转为int类型
     * @param bytes
     * @param offset
     * @return
     */
    public static int bytes2int(byte[] bytes, int offset){
        return (bytes[offset + 3] & 0xFF) + ((bytes[offset + 2] & 0xFF) << 8) + ((bytes[offset + 1] & 0xFF) << 16) + (bytes[offset] << 24);
    }

    /**
     * 把字节数组中offset开始的前8个字节，转为int类型
     * @param bytes
     * @param offset
     * @return
     */
    public static long bytes2long(byte[] bytes, int offset){
        return (bytes[offset + 7] & 0xFFL) + ((bytes[offset + 6] & 0xFFL) << 8) + ((bytes[offset + 5] & 0xFFL) << 16)
                + ((bytes[offset + 4] & 0xFFL) << 24) + ((bytes[offset + 3] & 0xFFL) << 32) + ((bytes[offset + 2] & 0xFFL) << 40)
                + ((bytes[offset + 1] & 0xFFL) << 48) + (((long) bytes[offset]) << 56);
    }

}
