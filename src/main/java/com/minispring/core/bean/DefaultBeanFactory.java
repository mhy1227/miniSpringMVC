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

    /**
     * 获取BeanDefinition
     */
    public BeanDefinition getBeanDefinition(String beanName) throws Exception {
        BeanDefinition bd = beanDefinitionMap.get(beanName);
        if (bd == null) {
            throw new Exception("No bean named '" + beanName + "' is defined");
        }
        return bd;
    }

    /**
     * 获取单例Bean
     */
    public Object getSingleton(String beanName) {
        return singletonObjects.get(beanName);
    }

    /**
     * 销毁Bean
     */
    public void destroyBean(String beanName, Object bean) {
        // 如果Bean实现了DisposableBean接口，调用其destroy方法
        if (bean instanceof DisposableBean) {
            try {
                ((DisposableBean) bean).destroy();
            } catch (Exception e) {
                // 记录错误但不抛出，确保其他Bean能够正常销毁
                e.printStackTrace();
            }
        }
        // 从单例缓存中移除
        singletonObjects.remove(beanName);
    }

    /**
     * 清理单例Bean缓存
     */
    public void clearSingletonCache() {
        singletonObjects.clear();
    }

    @Override
    public Object getBean(String name) throws Exception {
        // 获取Bean定义
        BeanDefinition beanDefinition = getBeanDefinition(name);

        // 如果是单例，先尝试从单例容器中获取
        if (beanDefinition.isSingleton()) {
            Object singleton = getSingleton(name);
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

    /**
     * 获取所有已注册的Bean定义名称
     */
    public String[] getBeanDefinitionNames() {
        return beanDefinitionMap.keySet().toArray(new String[0]);
    }
} 