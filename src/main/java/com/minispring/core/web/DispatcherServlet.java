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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * MVC核心分发器
 * 负责请求的分发和处理
 */
public class DispatcherServlet extends HttpServlet {
    private ApplicationContext applicationContext;
    private HandlerMapping handlerMapping;
    private ViewResolver viewResolver;
    private Properties properties;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
        try {
            // 1. 加载配置文件
            String contextConfigLocation = config.getInitParameter("contextConfigLocation");
            loadConfig(contextConfigLocation);
            
            // 2. 初始化ApplicationContext
            String basePackage = properties.getProperty("scan.package");
            applicationContext = new AnnotationConfigApplicationContext(basePackage);
            
            // 3. 初始化HandlerMapping
            handlerMapping = new RequestMappingHandlerMapping(applicationContext);
            
            // 4. 初始化ViewResolver
            initViewResolver();
            
        } catch (Exception e) {
            throw new ServletException("Failed to initialize DispatcherServlet", e);
        }
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

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        try {
            // 1. 查找处理器
            HandlerExecutionChain handler = handlerMapping.getHandler(req);
            if (handler == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            // 2. 调用处理器方法
            Object controller = handler.getHandler();
            Method method = handler.getHandlerMethod();
            
            // 3. 执行方法
            Object result = method.invoke(controller);
            
            // 4. 处理响应
            handleResponse(req, resp, result);
            
        } catch (Exception e) {
            throw new ServletException("Failed to handle request", e);
        }
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