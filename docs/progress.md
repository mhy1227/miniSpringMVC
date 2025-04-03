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

### 11.2 Controller 扫描失败问题
#### 问题描述
在启动应用后访问 `/mvc/hello.do` 时出现 404 错误。日志显示：
```
03-Apr-2025 10:32:40.132 信息 [http-nio-8080-exec-6] com.minispring.core.web.RequestMappingHandlerMapping.initialize Found 0 beans
03-Apr-2025 10:32:40.133 信息 [http-nio-8080-exec-6] com.minispring.core.web.RequestMappingHandlerMapping.initialize URL mappings initialized: []
```

虽然 `application.properties` 中正确配置了组件扫描路径：
```properties
scan.package=com.minispring.core,com.minispring.test.mvc
```
但是系统无法扫描到任何 Controller。

#### 根本原因
1. Controller 类（如 `UserController`）被错误地放置在 `src/test/java` 目录下
2. 在 Maven 项目中，`src/test/java` 目录下的类不会被打包到最终的 WAR 文件中
3. 导致 Tomcat 运行时无法找到这些 Controller 类
4. 最终导致组件扫描失败，没有找到任何可用的处理器映射

#### 解决方案
1. 将 Controller 类移动到正确的源代码目录：
   - 从：`src/test/java/com/minispring/test/mvc/`
   - 到：`src/main/java/com/minispring/test/mvc/`

2. 保持包结构和类内容不变：
```java
package com.minispring.test.mvc;

@Controller
public class UserController {
    @RequestMapping("/hello.do")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hello, " + name + "!";
    }
}
```

#### 技术要点
1. Maven 项目结构规范：
   - `src/main/java`: 项目源代码，会被打包到最终产物中
   - `src/test/java`: 测试代码，不会被打包到最终产物中
   - `src/main/resources`: 项目资源文件
   - `src/test/resources`: 测试资源文件

2. Spring MVC 组件扫描机制：
   - 扫描路径配置必须正确
   - 被扫描的类必须位于可访问的类路径下
   - 类必须有正确的注解（如 @Controller）

#### 相关影响
- 修复了 404 错误问题
- 确保了 Controller 能被正确扫描和注册
- 改进了项目结构，符合 Maven 标准

#### 经验总结
1. 在开发 Web 应用时，确保业务代码放在正确的源代码目录下
2. 理解构建工具（如 Maven）的项目结构约定
3. 当出现 404 错误时，检查组件扫描的日志，确认 Controller 是否被正确注册
4. 测试代码和生产代码要严格分离，放在正确的目录中 

### 11.3 JDK 版本兼容性问题
#### 环境信息
```
Java 环境变量:     D:\dev_tools\JAVA\jdk22
Java虚拟机版本:    22.0.1+8-16
```

#### 潜在风险
1. 项目在 `pom.xml` 中配置的是 JDK 1.8：
```xml
<properties>
    <maven.compiler.source>1.8</maven.compiler.source>
    <maven.compiler.target>1.8</maven.compiler.target>
</properties>
```

2. 而实际运行环境是 JDK 22，这可能会导致：
   - 编译时和运行时的不一致
   - 某些 JDK 8 特性在 JDK 22 中的行为变化
   - 反射和类加载行为的差异

#### 建议措施
1. 统一开发和运行环境：
   - 要么升级项目到 JDK 22
   - 要么降级运行环境到 JDK 8

2. 如果选择升级到 JDK 22：
   - 更新 `pom.xml` 的编译级别
   ```xml
   <properties>
       <maven.compiler.source>22</maven.compiler.source>
       <maven.compiler.target>22</maven.compiler.target>
   </properties>
   ```
   - 检查所有反射相关的代码
   - 确保第三方依赖兼容 JDK 22

3. 如果选择使用 JDK 8：
   - 修改 Tomcat 的 JRE 配置
   - 确保开发环境使用 JDK 8
   - 保持现有的 `pom.xml` 配置

#### 最佳实践
1. 开发环境和运行环境应该使用相同的 JDK 版本
2. 在项目初期就确定好 JDK 版本
3. 记录 JDK 版本要求在项目文档中
4. 使用 CI/CD 时注意指定正确的 JDK 版本 