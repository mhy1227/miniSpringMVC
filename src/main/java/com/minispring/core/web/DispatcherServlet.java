package com.minispring.core.web;

import com.minispring.core.context.AnnotationConfigApplicationContext;
import com.minispring.core.context.ApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * MVC核心分发器
 * 负责请求的分发和处理
 */
public class DispatcherServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(DispatcherServlet.class.getName());
    
    private ApplicationContext applicationContext;
    private HandlerMapping handlerMapping;
    private ViewResolver viewResolver;
    private List<HandlerMethodArgumentResolver> argumentResolvers;
    private Properties properties;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
        try {
            logger.info("Initializing DispatcherServlet...");
            
            // 1. 加载配置文件
            String contextConfigLocation = config.getInitParameter("contextConfigLocation");
            loadConfig(contextConfigLocation);
            
            // 2. 初始化ApplicationContext
            String basePackage = properties.getProperty("scan.package");
            applicationContext = new AnnotationConfigApplicationContext(basePackage);
            applicationContext.refresh(); // 刷新上下文
            
            // 3. 初始化HandlerMapping
            handlerMapping = new RequestMappingHandlerMapping(applicationContext);
            
            // 4. 初始化ViewResolver
            initViewResolver();
            
            // 5. 初始化参数解析器
            initArgumentResolvers();
            
            logger.info("DispatcherServlet initialization completed");
        } catch (Exception e) {
            logger.severe("Failed to initialize DispatcherServlet: " + e.getMessage());
            throw new ServletException("Failed to initialize DispatcherServlet", e);
        }
    }

    private void initArgumentResolvers() {
        argumentResolvers = new ArrayList<>();
        argumentResolvers.add(new RequestParamArgumentResolver());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        try {
            logger.info("Processing request: " + req.getRequestURI());
            
            // 1. 查找处理器
            HandlerExecutionChain handler = handlerMapping.getHandler(req);
            if (handler == null) {
                logger.warning("No handler found for request: " + req.getRequestURI());
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            // 2. 准备方法参数
            Object controller = handler.getHandler();
            Method method = handler.getHandlerMethod();
            Object[] args = resolveHandlerArguments(method, req);
            
            // 3. 调用处理器方法
            Object result = method.invoke(controller, args);
            
            // 4. 处理响应
            handleResponse(req, resp, result);
            
        } catch (Exception e) {
            logger.severe("Failed to handle request: " + e.getMessage());
            throw new ServletException("Failed to handle request", e);
        }
    }

    private Object[] resolveHandlerArguments(Method method, HttpServletRequest request) throws Exception {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];
        
        for (int i = 0; i < parameters.length; i++) {
            MethodParameter methodParameter = new MethodParameter(method, i);
            args[i] = resolveArgument(methodParameter, request);
        }
        
        return args;
    }

    private Object resolveArgument(MethodParameter parameter, HttpServletRequest request) throws Exception {
        for (HandlerMethodArgumentResolver resolver : argumentResolvers) {
            if (resolver.supportsParameter(parameter)) {
                return resolver.resolveArgument(parameter, request);
            }
        }
        throw new IllegalArgumentException(
            "Unsupported parameter type: " + parameter.getParameterType().getName());
    }

    /**
     * 处理响应
     */
    private void handleResponse(HttpServletRequest request, HttpServletResponse response, Object result) throws Exception {
        if (result == null) {
            return;
        }
        
        // 如果返回值是字符串，尝试解析为视图
        if (result instanceof String) {
            String viewName = (String) result;
            View view = viewResolver.resolveViewName(viewName);
            Map<String, Object> model = new HashMap<>(); // TODO: 从请求中获取模型数据
            view.render(model, request, response);
            return;
        }
        
        // 如果是其他类型，当作普通响应处理
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
        response.getWriter().write(String.valueOf(result));
    }

    /**
     * 初始化视图解析器
     */
    private void initViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix(properties.getProperty("mvc.view.prefix", "/WEB-INF/views/"));
        resolver.setSuffix(properties.getProperty("mvc.view.suffix", ".jsp"));
        this.viewResolver = resolver;
    }

    /**
     * 加载配置文件
     */
    private void loadConfig(String location) throws IOException {
        properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream(
            location.replace("classpath:", "")));
    }

    @Override
    public void destroy() {
        try {
            if (applicationContext != null) {
                applicationContext.close();
            }
        } catch (Exception e) {
            // 记录错误但不抛出，确保其他资源能够正常销毁
            e.printStackTrace();
        }
        super.destroy();
    }
} 