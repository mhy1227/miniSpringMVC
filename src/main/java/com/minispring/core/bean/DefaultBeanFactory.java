package com.minispring.core.bean;

import com.minispring.core.annotation.Autowired;
import com.minispring.core.util.ReflectionUtil;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BeanFactory的默认实现类
 */
public class DefaultBeanFactory implements BeanFactory {
    // 存储Bean定义的容器
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    // 存储单例Bean的容器
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    /**
     * 注册BeanDefinition
     * @param beanName bean的名称
     * @param beanDefinition bean的定义
     */
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    public Object getBean(String name) throws Exception {
        // 获取Bean定义
        BeanDefinition beanDefinition = beanDefinitionMap.get(name);
        if (beanDefinition == null) {
            throw new Exception("No bean named '" + name + "' is defined");
        }

        // 如果是单例，先尝试从单例容器中获取
        if (beanDefinition.isSingleton()) {
            Object singleton = singletonObjects.get(name);
            if (singleton == null) {
                singleton = createBean(beanDefinition);
                singletonObjects.put(name, singleton);
            }
            return singleton;
        }

        // 如果是原型，则创建新的实例
        return createBean(beanDefinition);
    }

    @Override
    public <T> T getBean(Class<T> clazz) throws Exception {
        // 遍历beanDefinitionMap，查找类型匹配的bean
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            BeanDefinition beanDefinition = entry.getValue();
            if (clazz.getName().equals(beanDefinition.getBeanClassName())) {
                return (T) getBean(entry.getKey());
            }
        }
        throw new Exception("No bean of type '" + clazz.getName() + "' is defined");
    }

    /**
     * 创建Bean实例
     * @param beanDefinition bean的定义
     * @return bean实例
     */
    private Object createBean(BeanDefinition beanDefinition) throws Exception {
        // 创建实例
        Object bean = ReflectionUtil.createInstance(beanDefinition.getBeanClassName());
        
        // 注入依赖
        injectDependencies(bean);
        
        return bean;
    }

    /**
     * 注入依赖
     */
    private void injectDependencies(Object bean) throws Exception {
        Class<?> beanClass = bean.getClass();
        
        // 处理字段注入
        for (Field field : beanClass.getDeclaredFields()) {
            Autowired autowired = field.getAnnotation(Autowired.class);
            if (autowired != null) {
                Object dependencyBean = null;
                try {
                    // 根据类型注入
                    dependencyBean = getBean(field.getType());
                } catch (Exception e) {
                    // 如果required为true，则抛出异常；否则忽略
                    if (autowired.required()) {
                        throw new Exception("Could not autowire field: " + field.getName(), e);
                    }
                    continue;
                }
                
                // 设置字段值
                ReflectionUtil.setField(field, bean, dependencyBean);
            }
        }
    }
} 