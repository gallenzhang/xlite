package com.xlite.common;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 反射工具类
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/3
 **/
public class ReflectUtil {

    /**
     * 分隔符
     */
    public static final String PARAM_CLASS_SPLIT = ",";

    /**
     * 空参
     */
    public static final String PARAM_EMPTY = "void";



    /**
     * name <==> class 缓存
     */
    private static final Map<String,Class> name2Class = new ConcurrentHashMap<>();
    private static final Map<Class,String> class2Name = new ConcurrentHashMap<>();

    /**
     * 基本数据类型缓存
     */
    private static final Map<String,Class<?>> PRIMITIVE_CLASS_MAP = new HashMap<>();

    static {
        PRIMITIVE_CLASS_MAP.put("boolean",boolean.class);
        PRIMITIVE_CLASS_MAP.put("byte",byte.class);
        PRIMITIVE_CLASS_MAP.put("char",char.class);
        PRIMITIVE_CLASS_MAP.put("short",short.class);
        PRIMITIVE_CLASS_MAP.put("double",double.class);
        PRIMITIVE_CLASS_MAP.put("float",float.class);
        PRIMITIVE_CLASS_MAP.put("int",int.class);
        PRIMITIVE_CLASS_MAP.put("long",long.class);
        PRIMITIVE_CLASS_MAP.put("void",Void.TYPE);
    }

    /**
     * 方法参数字符串，以逗号分隔。如果没有参数，用void表示
     * @param method
     * @return
     */
    public static String getMethodParamDesc(Method method){
        Class<?>[] parameterTypes = method.getParameterTypes();
        if(parameterTypes == null || parameterTypes.length == 0){
            return StringUtils.EMPTY;
        }

        StringBuilder sb = new StringBuilder();
        for (Class<?> clazz : parameterTypes){
            String className = getName(clazz);
            sb.append(className).append(PARAM_CLASS_SPLIT);
        }
        return sb.substring(0,sb.length() - 1);
    }

    /**
     * 将Class转为字符串
     * @param clazz
     * @return
     */
    public static String getName(Class<?> clazz) {
        if(clazz == null){
            return null;
        }

        if(class2Name.get(clazz) != null){
            return class2Name.get(clazz);
        }

        String className = getNameWithoutCache(clazz);
        class2Name.put(clazz,className);

        return className;

    }

    /**
     * Class转为className，注意这里需要对数组进行处理
     * @param clazz
     * @return
     */
    private static String getNameWithoutCache(Class<?> clazz) {
        if(!clazz.isArray()){
            return clazz.getName();
        }

        StringBuilder sb = new StringBuilder();
        while (clazz.isArray()){
            sb.append("[]");
            clazz = clazz.getComponentType();
        }

        return clazz.getName() +sb.toString();
    }


    /**
     * 类型名称转成 Class
     * @param names
     * @return
     */
    public static Class<?>[] forNames(String names) throws ClassNotFoundException {
        if(names == null || names.length() == 0){
            return new Class<?>[0];
        }

        //类名数组
        String[] classNames = names.split(PARAM_CLASS_SPLIT);

        Class<?>[] classTypes = new Class<?>[classNames.length];
        for(int i = 0; i < classNames.length; i++){
            String className = classNames[i];
            classTypes[i] = forName(className);
        }
        return classTypes;
    }

    /**
     * 通过类名生成Class
     * @param className
     * @return
     */
    public static Class<?> forName(String className) throws ClassNotFoundException {
        if(className == null || className.length() == 0){
            return null;
        }

        //从缓存中取
        if(name2Class.get(className) != null){
            return name2Class.get(className);
        }

        Class<?> clazz = forNameWithoutCache(className);
        name2Class.put(className,clazz);

        return clazz;
    }

    /**
     * class名称转Class
     * @param className
     * @return
     */
    private static Class<?> forNameWithoutCache(String className) throws ClassNotFoundException {
        //非数组类型
        if(!className.endsWith("[]")){
            Class<?> clazz = getPrimitiveClass(className);
            clazz = clazz == null ? Class.forName(className,true,Thread.currentThread().getContextClassLoader()) : clazz;
            return clazz;
        }

        int dimensionSize = 0;
        while (className.endsWith("[]")){
            dimensionSize ++;
            className = className.substring(0,className.length() - 2);
        }

        int[] dimensions = new int[dimensionSize];
        Class<?> clazz = getPrimitiveClass(className);
        if(clazz == null){
            clazz = Class.forName(className,true,Thread.currentThread().getContextClassLoader());
        }

        return Array.newInstance(clazz,dimensions).getClass();
    }

    /**
     * 获取基本类型Class
     * @param className
     * @return
     */
    public static Class<?> getPrimitiveClass(String className){
        if(className.length() <= 7){
            return PRIMITIVE_CLASS_MAP.get(className);
        }
        return null;
    }
}
