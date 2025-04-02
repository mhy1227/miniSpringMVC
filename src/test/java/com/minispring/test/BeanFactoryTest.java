package com.minispring.test;

import com.minispring.core.bean.DefaultBeanDefinition;
import com.minispring.core.bean.DefaultBeanFactory;
import com.minispring.test.bean.TestBean;
import org.junit.Assert;
import org.junit.Test;

public class BeanFactoryTest {
    
    @Test
    public void testBeanFactory() throws Exception {
        // 1. 创建bean工厂
        DefaultBeanFactory beanFactory = new DefaultBeanFactory();

        // 2. 创建bean定义
        DefaultBeanDefinition beanDefinition = new DefaultBeanDefinition();
        beanDefinition.setBeanClassName(TestBean.class.getName());
        
        // 3. 注册bean定义
        beanFactory.registerBeanDefinition("testBean", beanDefinition);

        // 4. 测试根据名称获取bean
        TestBean testBean1 = (TestBean) beanFactory.getBean("testBean");
        Assert.assertNotNull(testBean1);
        Assert.assertEquals("Hello MiniSpring", testBean1.getMessage());

        // 5. 测试根据类型获取bean
        TestBean testBean2 = beanFactory.getBean(TestBean.class);
        Assert.assertNotNull(testBean2);
        Assert.assertEquals("Hello MiniSpring", testBean2.getMessage());

        // 6. 验证单例
        Assert.assertSame("应该是同一个实例", testBean1, testBean2);
    }

    @Test(expected = Exception.class)
    public void testGetNonExistentBean() throws Exception {
        DefaultBeanFactory beanFactory = new DefaultBeanFactory();
        beanFactory.getBean("nonExistentBean");
    }
} 