package com.minispring.core.context;

import com.minispring.core.bean.BeanFactory;

/**
 * 应用上下文接口
 * 扩展BeanFactory，提供更多的应用级别的功能
 */
public interface ApplicationContext extends BeanFactory {
    /**
     * 刷新应用上下文
     * 包括扫描类、注册Bean定义、初始化Bean等
     */
    void refresh() throws Exception;

    /**
     * 关闭应用上下文
     * 执行销毁操作
     */
    void close() throws Exception;

    /**
     * 获取应用上下文的配置路径
     */
    String getConfigLocation();

    /**
     * 获取所有已注册的Bean名称
     */
    String[] getBeanDefinitionNames();
} 