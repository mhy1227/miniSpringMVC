# MiniSpring MVC 项目进度

## 一、项目准备状态

### 1.1 基础环境 ✅
- [x] JDK 8+ 配置
- [x] Maven 3.6+ 配置
- [x] IDE 配置
- [x] Git 配置
- [x] Tomcat 8+ 配置

### 1.2 项目配置 ✅
- [x] pom.xml 依赖配置
- [x] .gitignore 配置
- [x] README.md 文档
- [x] 开发规范文档
- [x] API设计文档

### 1.3 项目结构 ✅
- [x] 基础目录结构
- [x] 核心包结构
  - com.minispring.core.annotation
  - com.minispring.core.bean
  - com.minispring.core.web
  - com.minispring.core.util
- [x] 配置文件
  - web.xml (已创建)
  - application.properties (已创建)
- [x] 测试目录
  - src/test/java
  - src/test/resources

## 二、核心功能开发计划

### 2.1 注解模块 📝
- [x] @Controller
  - 功能：标记控制器类
  - 状态：已实现
  
- [x] @Service
  - 功能：标记服务类
  - 状态：已实现
  
- [x] @Autowired
  - 功能：依赖注入
  - 状态：已实现
  
- [x] @RequestMapping
  - 功能：URL映射
  - 状态：已实现

- [x] @Component
  - 功能：基础组件注解
  - 状态：已实现

### 2.2 IoC容器 📝
- [x] BeanFactory
  - 功能：Bean工厂接口
  - 状态：基础功能已实现
    - 完成Bean的基本管理
    - 实现依赖注入（字段注入）
    - 支持单例和原型作用域
  
- [x] ApplicationContext
  - 功能：应用上下文
  - 状态：已实现
    - 完成Bean的扫描和注册
    - 实现Bean的生命周期管理
    - 支持Bean的初始化和销毁
    - 提供应用上下文状态管理
  
- [x] BeanDefinition
  - 功能：Bean定义
  - 状态：已实现

### 2.3 MVC核心 📝
- [x] DispatcherServlet
  - 功能：核心分发器
  - 状态：基础功能已实现
    - 支持配置文件加载
    - 集成ApplicationContext
    - 集成HandlerMapping
    - 基本的请求分发
    - 简单的响应处理
    - 生命周期管理
  
- [x] HandlerMapping
  - 功能：处理器映射
  - 状态：已实现
    - 支持类级别和方法级别的@RequestMapping
    - 支持URL到处理方法的映射
    - 提供处理器执行链
    - URL路径标准化处理
    - 重复URL检测
    - 线程安全的延迟初始化
  
- [x] ViewResolver
  - 功能：视图解析器
  - 状态：基础功能已实现
    - 完成 View 接口定义
    - 完成 ViewResolver 接口定义
    - 实现 JSP 视图支持
    - 支持视图前缀和后缀配置
    - 集成到 DispatcherServlet

### 2.4 工具类 📝
- [x] ClassScanner
  - 功能：类扫描器
  - 状态：基础功能已实现
    - 支持扫描指定包下的所有类
    - 支持按注解过滤类
  
- [x] ReflectionUtil
  - 功能：反射工具类
  - 状态：已实现

### 2.5 MVC功能增强
- [ ] 请求参数绑定
  - 功能：将HTTP请求参数绑定到控制器方法参数
  - 状态：基础设施已完成
    - ✅ @RequestParam 注解定义
    - ✅ MethodParameter 参数封装类
    - ✅ HandlerMethodArgumentResolver 接口
    - ⚠️ RequestParamArgumentResolver 实现中
    - ⚠️ 参数解析器集成到 DispatcherServlet
- [ ] JSON响应处理
  - 功能：支持JSON格式的响应数据
  - 状态：待实现
- [ ] 异常处理机制
  - 功能：统一的异常处理
  - 状态：待实现

## 三、测试计划

### 3.1 单元测试 📝
- [ ] 注解测试
- [ ] IoC容器测试
- [ ] MVC功能测试
- [ ] 工具类测试

### 3.2 集成测试 📝
- [ ] Web环境测试
- [ ] 完整流程测试
- [ ] 性能测试

## 四、示例应用开发

### 4.1 基础CRUD示例 📝
- [ ] 用户管理模块
- [ ] RESTful API
- [ ] 页面模板

### 4.2 高级特性示例 📝
- [ ] 文件上传下载
- [ ] JSON数据交互
- [ ] 拦截器示例

## 五、文档编写

### 5.1 技术文档 📝
- [ ] 架构设计文档
- [ ] API文档
- [ ] 测试报告

### 5.2 使用文档 📝
- [ ] 安装部署指南
- [ ] 使用教程
- [ ] 常见问题

## 六、发布计划

### 6.1 v1.0版本 📅
- [ ] 核心功能完成
- [ ] 文档完善
- [ ] 示例应用
- [ ] 单元测试覆盖率 > 80%

### 6.2 v1.1版本 📅
- [ ] 性能优化
- [ ] 功能扩展
- [ ] Bug修复

## 图例
- ✅ 已完成
- ⚠️ 进行中
- 📝 待开始
- 📅 计划中

## 十一、问题记录与解决方案

### 11.1 注解类实例化问题
#### 问题描述
在将项目从 javax.servlet 迁移到 jakarta.servlet 后，启动 Tomcat 10.1.28 时出现以下错误：
```
java.lang.NoSuchMethodException: com.minispring.core.annotation.Controller.<init>()
```

错误原因：在组件扫描过程中，系统错误地尝试实例化注解类型（如 @Controller、@Component 等）。这是不正确的，因为注解类型不应该被实例化。

#### 解决方案
修改 `ClassScanner` 类的 `scanWithAnnotation` 方法，添加对注解类型的过滤：
```java
public static List<Class<?>> scanWithAnnotation(String basePackage, 
        Class<? extends Annotation> annotation) throws Exception {
    List<String> classNames = scanPackage(basePackage);
    List<Class<?>> result = new ArrayList<>();
    
    for (String className : classNames) {
        Class<?> clazz = Class.forName(className);
        // 添加 !clazz.isAnnotation() 条件来过滤掉注解类型
        if (!clazz.isAnnotation() && clazz.isAnnotationPresent(annotation)) {
            result.add(clazz);
        }
    }
    
    return result;
}
```

#### 技术要点
1. 使用 `Class.isAnnotation()` 方法判断一个类是否为注解类型
2. 在扫描带有 @Component 注解的类时，确保不包含注解类型本身
3. 这个修改确保了只有实际的组件类（如控制器、服务类等）会被注册为 Bean

#### 相关影响
- 解决了 Tomcat 启动时的 NoSuchMethodException 错误
- 优化了组件扫描逻辑，避免了不必要的注解类实例化
- 提高了框架的健壮性 