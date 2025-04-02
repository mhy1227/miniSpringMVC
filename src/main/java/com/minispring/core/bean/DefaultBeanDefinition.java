package com.minispring.core.bean;

/**
 * BeanDefinition的默认实现类
 */
public class DefaultBeanDefinition implements BeanDefinition {
    private String beanClassName;
    private String scope = "singleton"; // 默认作用域为单例

    @Override
    public String getBeanClassName() {
        return this.beanClassName;
    }

    @Override
    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    @Override
    public String getScope() {
        return this.scope;
    }

    @Override
    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public boolean isSingleton() {
        return "singleton".equals(this.scope);
    }

    @Override
    public boolean isPrototype() {
        return "prototype".equals(this.scope);
    }
} 