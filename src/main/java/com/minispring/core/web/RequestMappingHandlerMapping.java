package com.minispring.core.web;

import com.minispring.core.annotation.Controller;
import com.minispring.core.annotation.RequestMapping;
import com.minispring.core.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 基于@RequestMapping注解的处理器映射实现
 */
public class RequestMappingHandlerMapping implements HandlerMapping {
    private final Map<String, HandlerExecutionChain> urlHandlers = new HashMap<>();
    private final ApplicationContext applicationContext;
    private boolean initialized = false;

    public RequestMappingHandlerMapping(ApplicationContext applicationContext) {
        this.applicationContext = Objects.requireNonNull(applicationContext, "ApplicationContext must not be null");
    }

    /**
     * 初始化URL和处理器的映射关系
     */
    public synchronized void initialize() {
        if (initialized) {
            return;
        }

        // 获取所有bean的名称
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        
        try {
            for (String beanName : beanNames) {
                Object bean = applicationContext.getBean(beanName);
                Class<?> beanClass = bean.getClass();
                
                // 只处理带有@Controller注解的类
                if (beanClass.isAnnotationPresent(Controller.class)) {
                    // 处理类级别的@RequestMapping
                    String baseUrl = "";
                    if (beanClass.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping requestMapping = beanClass.getAnnotation(RequestMapping.class);
                        baseUrl = normalizePath(requestMapping.value());
                    }
                    
                    // 处理方法级别的@RequestMapping
                    Method[] methods = beanClass.getDeclaredMethods();
                    for (Method method : methods) {
                        if (method.isAnnotationPresent(RequestMapping.class)) {
                            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                            String methodPath = normalizePath(requestMapping.value());
                            String url = baseUrl + methodPath;
                            
                            // 检查URL是否已存在
                            if (urlHandlers.containsKey(url)) {
                                throw new IllegalStateException(
                                    "Duplicate mapping found for URL path [" + url + "]: " +
                                    urlHandlers.get(url) + " and " + method
                                );
                            }
                            
                            // 创建处理器执行链并存储
                            HandlerExecutionChain handler = new HandlerExecutionChain(bean, method);
                            urlHandlers.put(url, handler);
                        }
                    }
                }
            }
            initialized = true;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize handler mappings", e);
        }
    }

    @Override
    public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        if (!initialized) {
            initialize();
        }

        // 获取请求路径
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String url = normalizePath(requestURI.substring(contextPath.length()));
        
        // 查找对应的处理器
        return urlHandlers.get(url);
    }

    /**
     * 标准化路径
     * 1. 确保以/开头
     * 2. 去除末尾的/（除非是根路径）
     * 3. 处理多余的/
     */
    private String normalizePath(String path) {
        if (path == null || path.isEmpty()) {
            return "/";
        }
        
        // 确保以/开头
        String normalized = path.startsWith("/") ? path : "/" + path;
        
        // 去除末尾的/（除非是根路径）
        if (normalized.length() > 1 && normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        
        // 处理多余的/
        return normalized.replaceAll("/+", "/");
    }
} 