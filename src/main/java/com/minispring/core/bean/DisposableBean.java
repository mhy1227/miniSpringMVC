package com.minispring.core.bean;

/**
 * Bean销毁接口
 * 实现此接口的Bean会在容器关闭时调用destroy方法
 */
public interface DisposableBean {
    /**
     * Bean销毁时的回调方法
     */
    void destroy() throws Exception;
} 