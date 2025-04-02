package com.minispring.test;

import com.minispring.core.annotation.Component;
import com.minispring.core.context.AnnotationConfigApplicationContext;
import org.junit.Assert;
import org.junit.Test;

@Component
public class ApplicationContextTest {
    
    @Test
    public void testApplicationContext() throws Exception {
        // 创建应用上下文
        AnnotationConfigApplicationContext context = 
            new AnnotationConfigApplicationContext("com.minispring.test");
        
        // 刷新上下文，触发扫描和注册Bean
        context.refresh();
        
        // 验证Bean是否被正确注册和管理
        String[] beanNames = context.getBeanDefinitionNames();
        Assert.assertTrue("应该至少注册了一个Bean", beanNames.length > 0);
        
        // 获取当前测试类的Bean（因为标注了@Component）
        ApplicationContextTest testBean = context.getBean(ApplicationContextTest.class);
        Assert.assertNotNull("应该能获取到测试类的Bean", testBean);
        
        // 关闭上下文
        context.close();
    }
} 