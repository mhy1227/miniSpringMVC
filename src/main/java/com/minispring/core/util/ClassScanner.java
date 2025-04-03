package com.minispring.core.util;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

/**
 * 类扫描器，用于扫描指定包下的类
 */
public class ClassScanner {
    private static final Logger logger = Logger.getLogger(ClassScanner.class.getName());

    /**
     * 扫描指定包下所有类
     * @param basePackage 基础包名
     * @return 类的完全限定名列表
     */
    public static List<String> scanPackage(String basePackage) throws IOException {
        List<String> classNames = new ArrayList<>();
        String path = basePackage.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        
        logger.info("Scanning package: " + basePackage + ", path: " + path);
        
        Enumeration<URL> resources = classLoader.getResources(path);
        if (!resources.hasMoreElements()) {
            logger.warning("No resources found for path: " + path);
        }
        
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            String protocol = resource.getProtocol();
            
            logger.info("Found resource: " + resource + " with protocol: " + protocol);
            
            if ("file".equals(protocol)) {
                String filePath = URLDecoder.decode(resource.getFile(), "UTF-8");
                logger.info("Scanning directory: " + filePath);
                scanDirectory(new File(filePath), basePackage, classNames);
            } else if ("jar".equals(protocol)) {
                logger.info("Scanning JAR: " + resource);
                scanJar(resource, path, classNames);
            }
        }
        
        logger.info("Found classes: " + classNames);
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
        
        logger.info("Scanning for annotation: " + annotation.getName());
        
        for (String className : classNames) {
            try {
                logger.info("Loading class: " + className);
                Class<?> clazz = Class.forName(className);
                if (!clazz.isAnnotation() && hasAnnotation(clazz, annotation)) {
                    logger.info("Found annotated class: " + className);
                    result.add(clazz);
                }
            } catch (Throwable e) {
                logger.warning("Failed to load class: " + className + ", error: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        logger.info("Found annotated classes: " + result);
        return result;
    }

    /**
     * 递归检查类是否包含指定注解（包括元注解）
     */
    private static boolean hasAnnotation(Class<?> clazz, Class<? extends Annotation> targetAnnotation) {
        if (clazz.isAnnotationPresent(targetAnnotation)) {
            return true;
        }
        
        // 检查类上的所有注解
        for (Annotation annotation : clazz.getAnnotations()) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            // 避免处理Java内置注解
            if (annotationType.getName().startsWith("java.lang.annotation")) {
                continue;
            }
            // 递归检查注解上的注解
            if (hasAnnotation(annotationType, targetAnnotation)) {
                return true;
            }
        }
        
        return false;
    }

    private static void scanDirectory(File directory, String basePackage, 
            List<String> classNames) {
        if (!directory.exists()) {
            logger.warning("Directory does not exist: " + directory);
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
                    logger.info("Found class file: " + className);
                    classNames.add(className);
                }
            }
        }
    }

    private static void scanJar(URL url, String path, List<String> classNames) throws IOException {
        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
        JarFile jarFile = jarURLConnection.getJarFile();
        
        logger.info("Scanning JAR file: " + jarFile.getName());
        
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String entryName = jarEntry.getName();
            
            if (entryName.startsWith(path) && entryName.endsWith(".class")) {
                String className = entryName.substring(0, entryName.length() - 6)
                    .replace('/', '.');
                logger.info("Found class in JAR: " + className);
                classNames.add(className);
            }
        }
    }
} 