package com.minispring.test;

import com.minispring.core.bean.DefaultBeanDefinition;
import com.minispring.core.bean.DefaultBeanFactory;
import com.minispring.test.bean.ControllerBean;
import com.minispring.test.bean.ServiceBean;
import org.junit.Assert;
import org.junit.Test;

public class AutowiredTest {
    
    @Test
    public void testAutowired() throws Exception {
        // 1. 创建bean工厂
        DefaultBeanFactory beanFactory = new DefaultBeanFactory();

        // 2. 注册ServiceBean
        DefaultBeanDefinition serviceBeanDefinition = new DefaultBeanDefinition();
        serviceBeanDefinition.setBeanClassName(ServiceBean.class.getName());
        beanFactory.registerBeanDefinition("serviceBean", serviceBeanDefinition);

        // 3. 注册ControllerBean
        DefaultBeanDefinition controllerBeanDefinition = new DefaultBeanDefinition();
        controllerBeanDefinition.setBeanClassName(ControllerBean.class.getName());
        beanFactory.registerBeanDefinition("controllerBean", controllerBeanDefinition);

        // 4. 获取controller实例并测试
        ControllerBean controllerBean = beanFactory.getBean(ControllerBean.class);
        
        // 验证依赖注入是否成功
        Assert.assertNotNull("Controller不应该为空", controllerBean);
        Assert.assertNotNull("注入的Service不应该为空", controllerBean.getServiceBean());
        Assert.assertEquals("Hello from ServiceBean", controllerBean.handleRequest());
    }
} 