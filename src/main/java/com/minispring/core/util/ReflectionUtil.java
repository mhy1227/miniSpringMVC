package com.minispring.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射工具类
 */
public class ReflectionUtil {
    
    /**
     * 创建实例
     */
    public static Object createInstance(String className) throws Exception {
        Class<?> clazz = Class.forName(className);
        return clazz.newInstance();
    }
    
    /**
     * 设置字段值
     */
    public static void setField(Field field, Object target, Object value) throws Exception {
        field.setAccessible(true);
        field.set(target, value);
    }
    
    /**
     * 获取字段值
     */
    public static Object getField(Field field, Object target) throws Exception {
        field.setAccessible(true);
        return field.get(target);
    }
    
    /**
     * 调用方法
     */
    public static Object invokeMethod(Method method, Object target, Object... args) throws Exception {
        method.setAccessible(true);
        return method.invoke(target, args);
    }
} 