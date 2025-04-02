package com.minispring.core.web;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 处理器执行链
 * 包含处理器对象和对应的处理方法
 */
public class HandlerExecutionChain {
    private final Object handler;
    private final Method handlerMethod;

    public HandlerExecutionChain(Object handler, Method handlerMethod) {
        this.handler = Objects.requireNonNull(handler, "Handler must not be null");
        this.handlerMethod = Objects.requireNonNull(handlerMethod, "HandlerMethod must not be null");
    }

    public Object getHandler() {
        return handler;
    }

    public Method getHandlerMethod() {
        return handlerMethod;
    }

    @Override
    public String toString() {
        return "HandlerExecutionChain{" +
                "handler=" + handler.getClass().getSimpleName() +
                ", method=" + handlerMethod.getName() +
                '}';
    }
} 