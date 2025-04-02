package com.minispring.core.bean;

/**
 * Bean工厂接口
 * 定义获取Bean的最基本方法
 */
public interface BeanFactory {
    /**
     * 根据bean的名称获取bean实例
     * @param name bean的名称
     * @return bean实例
     * @throws Exception 如果获取bean失败
     */
    Object getBean(String name) throws Exception;

    /**
     * 根据bean的类型获取bean实例
     * @param clazz bean的类型
     * @return bean实例
     * @throws Exception 如果获取bean失败
     */
    <T> T getBean(Class<T> clazz) throws Exception;
} 