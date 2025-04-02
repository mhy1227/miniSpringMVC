package com.minispring.core.util;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 类扫描器，用于扫描指定包下的类
 */
public class ClassScanner {

    /**
     * 扫描指定包下所有类
     * @param basePackage 基础包名
     * @return 类的完全限定名列表
     */
    public static List<String> scanPackage(String basePackage) throws IOException {
        List<String> classNames = new ArrayList<>();
        String path = basePackage.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        
        Enumeration<URL> resources = classLoader.getResources(path);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            File file = new File(resource.getFile());
            scanDirectory(file, basePackage, classNames);
        }
        
        return classNames;
    }

    /**
     * 扫描指定包下带有特定注解的类
     * @param basePackage 基础包名
     * @param annotation 目标注解类
     * @return 带有指定注解的类的Class对象列表
     */
    public static List<Class<?>> scanWithAnnotation(String basePackage, 
            Class<? extends Annotation> annotation) throws Exception {
        List<String> classNames = scanPackage(basePackage);
        List<Class<?>> result = new ArrayList<>();
        
        for (String className : classNames) {
            Class<?> clazz = Class.forName(className);
            if (clazz.isAnnotationPresent(annotation)) {
                result.add(clazz);
            }
        }
        
        return result;
    }

    private static void scanDirectory(File directory, String basePackage, 
            List<String> classNames) {
        if (!directory.exists()) {
            return;
        }
        
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    scanDirectory(file, basePackage + "." + file.getName(), classNames);
                } else if (file.getName().endsWith(".class")) {
                    String className = basePackage + "." + 
                        file.getName().substring(0, file.getName().length() - 6);
                    classNames.add(className);
                }
            }
        }
    }
} 