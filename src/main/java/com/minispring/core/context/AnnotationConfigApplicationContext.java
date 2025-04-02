package com.minispring.core.context;

import com.minispring.core.annotation.Component;
import com.minispring.core.bean.BeanDefinition;
import com.minispring.core.bean.DefaultBeanDefinition;
import com.minispring.core.bean.DefaultBeanFactory;
import com.minispring.core.util.ClassScanner;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于注解的应用上下文实现
 * 扫描指定包下的所有类，将带有@Component及其派生注解的类注册为Bean
 */
public class AnnotationConfigApplicationContext implements ApplicationContext {
    private final DefaultBeanFactory beanFactory;
    private final String[] basePackages;
    private boolean active = false;

    public AnnotationConfigApplicationContext(String basePackage) {
        this.basePackages = basePackage.split(",");
        this.beanFactory = new DefaultBeanFactory();
    }

    @Override
    public void refresh() throws Exception {
        // 1. 扫描所有包下带有@Component注解的类
        List<Class<?>> allClasses = new ArrayList<>();
        for (String basePackage : basePackages) {
            List<Class<?>> classes = ClassScanner.scanWithAnnotation(basePackage.trim(), Component.class);
            allClasses.addAll(classes);
        }
        
        // 2. 注册Bean定义
        for (Class<?> clazz : allClasses) {
            DefaultBeanDefinition beanDefinition = new DefaultBeanDefinition();
            beanDefinition.setBeanClassName(clazz.getName());
            
            // 获取bean名称：默认使用类名首字母小写
            String beanName = toLowerFirstCase(clazz.getSimpleName());
            beanFactory.registerBeanDefinition(beanName, beanDefinition);
        }

        // 3. 初始化所有单例Bean
        String[] beanNames = beanFactory.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            if (beanDefinition.isSingleton()) {
                beanFactory.getBean(beanName);
            }
        }

        // 4. 标记上下文已激活
        active = true;
    }

    @Override
    public void close() throws Exception {
        if (!active) {
            return;
        }

        try {
            // 1. 销毁所有单例Bean
            String[] beanNames = beanFactory.getBeanDefinitionNames();
            for (String beanName : beanNames) {
                BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
                if (beanDefinition.isSingleton()) {
                    Object bean = beanFactory.getSingleton(beanName);
                    if (bean != null) {
                        beanFactory.destroyBean(beanName, bean);
                    }
                }
            }
        } finally {
            // 2. 清理上下文状态
            active = false;
            beanFactory.clearSingletonCache();
        }
    }

    @Override
    public String getConfigLocation() {
        return String.join(",", basePackages);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanFactory.getBeanDefinitionNames();
    }

    @Override
    public Object getBean(String name) throws Exception {
        return beanFactory.getBean(name);
    }

    @Override
    public <T> T getBean(Class<T> clazz) throws Exception {
        return beanFactory.getBean(clazz);
    }

    /**
     * 将字符串首字母小写
     */
    private String toLowerFirstCase(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        char[] chars = str.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }
} 