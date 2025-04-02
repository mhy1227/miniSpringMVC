package com.minispring.test;

import com.minispring.core.annotation.Component;
import com.minispring.core.util.ClassScanner;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ClassScannerTest {

    @Test
    public void testScanPackage() throws Exception {
        List<String> classNames = ClassScanner.scanPackage("com.minispring.test");
        Assert.assertFalse("应该能扫描到测试包中的类", classNames.isEmpty());
        Assert.assertTrue("应该包含当前测试类", 
            classNames.contains("com.minispring.test.ClassScannerTest"));
    }

    @Test
    public void testScanWithAnnotation() throws Exception {
        List<Class<?>> classes = ClassScanner.scanWithAnnotation(
            "com.minispring.test", Component.class);
        Assert.assertFalse("应该能扫描到带有@Component注解的类", classes.isEmpty());
        
        boolean found = false;
        for (Class<?> clazz : classes) {
            if (clazz.equals(ApplicationContextTest.class)) {
                found = true;
                break;
            }
        }
        Assert.assertTrue("应该能找到带有@Component注解的ApplicationContextTest类", found);
    }
} 