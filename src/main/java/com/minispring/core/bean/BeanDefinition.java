package com.minispring.core.bean;

/**
 * Bean定义接口
 * 用于描述Bean的基本信息
 */
public interface BeanDefinition {
    /**
     * 获取Bean的类名
     */
    String getBeanClassName();

    /**
     * 设置Bean的类名
     */
    void setBeanClassName(String beanClassName);

    /**
     * 获取Bean的作用域
     */
    String getScope();

    /**
     * 设置Bean的作用域
     */
    void setScope(String scope);

    /**
     * 是否是单例
     */
    boolean isSingleton();

    /**
     * 是否是原型
     */
    boolean isPrototype();
} 