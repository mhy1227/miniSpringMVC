package com.minispring.core.web;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 方法参数封装类
 * 用于存储方法参数的相关信息
 */
public class MethodParameter {
    private final Method method;
    private final int parameterIndex;
    private final Class<?> parameterType;
    private final String parameterName;
    private final Annotation[] annotations;

    public MethodParameter(Method method, int parameterIndex) {
        this.method = Objects.requireNonNull(method, "Method must not be null");
        this.parameterIndex = parameterIndex;
        this.parameterType = method.getParameterTypes()[parameterIndex];
        // TODO: 后续可以添加参数名称解析功能
        this.parameterName = "arg" + parameterIndex;
        this.annotations = method.getParameterAnnotations()[parameterIndex];
    }

    /**
     * 获取指定类型的参数注解
     */
    public <T extends Annotation> T getParameterAnnotation(Class<T> annotationType) {
        for (Annotation annotation : annotations) {
            if (annotationType.isInstance(annotation)) {
                return annotationType.cast(annotation);
            }
        }
        return null;
    }

    /**
     * 判断参数是否有指定类型的注解
     */
    public boolean hasParameterAnnotation(Class<? extends Annotation> annotationType) {
        return getParameterAnnotation(annotationType) != null;
    }

    public Method getMethod() {
        return method;
    }

    public int getParameterIndex() {
        return parameterIndex;
    }

    public Class<?> getParameterType() {
        return parameterType;
    }

    public String getParameterName() {
        return parameterName;
    }

    public Annotation[] getAnnotations() {
        return annotations;
    }
} 