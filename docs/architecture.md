# MiniSpring MVC 架构设计文档

## 一、整体架构

### 1.1 分层架构
```
+------------------+
|     Web层        |  DispatcherServlet, Controllers
+------------------+
|     MVC层        |  HandlerMapping, ViewResolver
+------------------+
|     IoC层        |  BeanFactory, ApplicationContext
+------------------+
|     工具层       |  ClassScanner, ReflectionUtil
+------------------+
```

### 1.2 核心组件
```
+------------------------+     +------------------+
|    DispatcherServlet   |<--->|   HandlerMapping |
+------------------------+     +------------------+
           |                           |
           v                           v
+------------------------+     +------------------+
|    HandlerAdapter      |<--->|   Controller     |
+------------------------+     +------------------+
           |                           |
           v                           v
+------------------------+     +------------------+
|    ViewResolver        |<--->|   View           |
+------------------------+     +------------------+
```

## 二、关键流程

### 2.1 请求处理流程
1. 请求进入DispatcherServlet
2. 通过HandlerMapping找到对应Controller
3. 通过HandlerAdapter调用Controller方法
4. 通过ViewResolver解析视图
5. 渲染视图并返回响应

### 2.2 IoC容器初始化流程
1. 扫描类文件
2. 解析注解
3. 创建BeanDefinition
4. 注册BeanDefinition
5. 实例化单例Bean
6. 注入依赖
7. 初始化Bean

## 三、核心模块设计

### 3.1 IoC容器
```
+-------------------+
|  BeanFactory      |
+-------------------+
         ^
         |
+-------------------+
| ApplicationContext|
+-------------------+
         ^
         |
+-------------------+
|  WebApplicationCtx|
+-------------------+
```

### 3.2 MVC框架
```
+-------------------+
|  DispatcherServlet|
+-------------------+
         |
    +----+----+
    |         |
+-------+ +--------+
|Handler| |View    |
|Mapping| |Resolver|
+-------+ +--------+
```

## 四、关键类设计

### 4.1 IoC相关类
```java
public interface BeanFactory {
    Object getBean(String name);
    <T> T getBean(Class<T> type);
    boolean containsBean(String name);
}

public interface ApplicationContext extends BeanFactory {
    void refresh();
    void close();
    String[] getBeanDefinitionNames();
}

public class BeanDefinition {
    private Class<?> beanClass;
    private String scope;
    private boolean isLazy;
    // ...
}
```

### 4.2 MVC相关类
```java
public class DispatcherServlet extends HttpServlet {
    private HandlerMapping handlerMapping;
    private ViewResolver viewResolver;
    // ...
}

public interface HandlerMapping {
    HandlerExecutionChain getHandler(HttpServletRequest request);
}

public interface ViewResolver {
    View resolveViewName(String viewName, Locale locale);
}
```

## 五、扩展点设计

### 5.1 Bean生命周期扩展点
- BeanFactoryPostProcessor
- BeanPostProcessor
- InitializingBean
- DisposableBean

### 5.2 MVC扩展点
- HandlerInterceptor
- HandlerExceptionResolver
- ViewResolver
- MessageConverter

## 六、关键技术实现

### 6.1 注解处理
- 运行时注解处理
- 反射机制
- 动态代理

### 6.2 依赖注入
- 构造器注入
- 字段注入
- 方法注入

### 6.3 AOP实现
- JDK动态代理
- CGLIB代理
- 切面织入

## 七、性能设计

### 7.1 缓存设计
- Bean实例缓存
- 注解信息缓存
- 反射元数据缓存

### 7.2 并发处理
- 线程安全的Bean容器
- 请求处理的并发控制
- 资源池化

## 八、安全设计

### 8.1 Web安全
- XSS防护
- CSRF防护
- SQL注入防护

### 8.2 配置安全
- 敏感信息加密
- 环境隔离
- 权限控制

## 九、监控设计

### 9.1 性能监控
- 请求处理时间
- Bean初始化时间
- 内存使用情况

### 9.2 日志监控
- 请求日志
- 错误日志
- 性能日志 