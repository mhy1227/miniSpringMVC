# MiniSpring MVC API设计文档

## 一、注解API

### 1.1 组件注解

#### @Controller
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {
    String value() default "";    // bean名称
}
```

#### @Service
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {
    String value() default "";    // bean名称
}
```

#### @Component
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
    String value() default "";    // bean名称
}
```

### 1.2 Web注解

#### @RequestMapping
```java
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
    String value() default "";           // URL路径
    RequestMethod[] method() default {}; // HTTP方法
}
```

#### @ResponseBody
```java
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseBody {
}
```

### 1.3 依赖注入注解

#### @Autowired
```java
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {
    boolean required() default true; // 是否必须
}
```

## 二、核心接口API

### 2.1 IoC容器接口

#### BeanFactory
```java
public interface BeanFactory {
    Object getBean(String name);
    <T> T getBean(Class<T> type);
    boolean containsBean(String name);
    Class<?> getType(String name);
    boolean isSingleton(String name);
}
```

#### ApplicationContext
```java
public interface ApplicationContext extends BeanFactory {
    void refresh();
    void close();
    String[] getBeanDefinitionNames();
    String getApplicationName();
    long getStartupDate();
}
```

### 2.2 MVC接口

#### HandlerMapping
```java
public interface HandlerMapping {
    HandlerExecutionChain getHandler(HttpServletRequest request);
    void registerHandler(String urlPath, Object handler);
}
```

#### ViewResolver
```java
public interface ViewResolver {
    View resolveViewName(String viewName, Locale locale);
}
```

#### HandlerAdapter
```java
public interface HandlerAdapter {
    boolean supports(Object handler);
    ModelAndView handle(HttpServletRequest request, 
                       HttpServletResponse response, 
                       Object handler);
}
```

## 三、工具类API

### 3.1 反射工具

#### ReflectionUtil
```java
public class ReflectionUtil {
    public static Object newInstance(Class<?> cls);
    public static Object getFieldValue(Object obj, Field field);
    public static void setFieldValue(Object obj, Field field, Object value);
    public static Method getMethod(Class<?> cls, String name, Class<?>... paramTypes);
}
```

### 3.2 类扫描工具

#### ClassScanner
```java
public class ClassScanner {
    public static Set<Class<?>> scan(String basePackage);
    public static Set<Class<?>> scanWithAnnotation(String basePackage, 
                                                 Class<? extends Annotation> annotation);
}
```

## 四、异常处理API

### 4.1 基础异常

#### BeanException
```java
public class BeanException extends RuntimeException {
    public BeanException(String message);
    public BeanException(String message, Throwable cause);
}
```

### 4.2 Web异常

#### HandlerException
```java
public class HandlerException extends RuntimeException {
    public HandlerException(String message);
    public HandlerException(String message, Throwable cause);
}
```

## 五、配置API

### 5.1 Bean定义

#### BeanDefinition
```java
public interface BeanDefinition {
    String getBeanClassName();
    void setBeanClassName(String beanClassName);
    String getScope();
    void setScope(String scope);
    boolean isSingleton();
    boolean isPrototype();
    boolean isLazyInit();
    void setLazyInit(boolean lazyInit);
}
```

### 5.2 配置加载

#### PropertyLoader
```java
public interface PropertyLoader {
    Properties loadProperties(String location);
    String getProperty(String key);
    String getProperty(String key, String defaultValue);
}
```

## 六、扩展点API

### 6.1 Bean生命周期接口

#### BeanPostProcessor
```java
public interface BeanPostProcessor {
    Object postProcessBeforeInitialization(Object bean, String beanName);
    Object postProcessAfterInitialization(Object bean, String beanName);
}
```

### 6.2 Web扩展接口

#### HandlerInterceptor
```java
public interface HandlerInterceptor {
    boolean preHandle(HttpServletRequest request, 
                     HttpServletResponse response, 
                     Object handler);
    void postHandle(HttpServletRequest request, 
                   HttpServletResponse response, 
                   Object handler, 
                   ModelAndView modelAndView);
    void afterCompletion(HttpServletRequest request, 
                        HttpServletResponse response, 
                        Object handler, 
                        Exception ex);
}
```

## 七、使用示例

### 7.1 控制器示例
```java
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public List<User> list() {
        return userService.findAll();
    }
}
```

### 7.2 服务层示例
```java
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
} 