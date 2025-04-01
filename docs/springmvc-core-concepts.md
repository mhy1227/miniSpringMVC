# Spring MVC 核心概念

## 一、整体架构

### 1.1 MVC模式
- **Model（模型）**：数据模型，提供要展示的数据
- **View（视图）**：负责展示数据的界面
- **Controller（控制器）**：负责接收请求，协调模型和视图

### 1.2 核心组件
```
+-------------------+     +------------------+     +------------------+
|  DispatcherServlet|---->| HandlerMapping   |---->| Controller       |
+-------------------+     +------------------+     +------------------+
         |                        |                        |
         |                +------------------+            |
         +--------------->| HandlerAdapter   |<-----------+
         |                +------------------+            |
         |                                               |
         |                +------------------+           |
         +--------------->| ViewResolver     |<----------+
                         +------------------+
```

## 二、核心组件详解

### 2.1 DispatcherServlet（前端控制器）
- Spring MVC 的核心组件
- 统一接收和处理所有HTTP请求
- 协调其他组件完成请求处理
- 管理请求的完整生命周期

### 2.2 HandlerMapping（处理器映射器）
- 维护 URL 和处理器的映射关系
- 根据请求找到对应的 Handler（处理器）
- 支持多种 URL 匹配方式
  - 精确匹配
  - 通配符匹配
  - 正则表达式匹配

### 2.3 HandlerAdapter（处理器适配器）
- 调用具体的 Handler 处理请求
- 将 Handler 的返回值转换为 ModelAndView
- 支持多种类型的处理器
- 处理请求参数绑定

### 2.4 ViewResolver（视图解析器）
- 将逻辑视图名解析为具体的 View 对象
- 支持多种视图技术（JSP、Thymeleaf等）
- 处理视图渲染的前缀和后缀配置

### 2.5 Handler（处理器）
- 实际处理请求的组件（通常是Controller）
- 执行业务逻辑
- 返回处理结果（ModelAndView）

## 三、工作流程

### 3.1 完整流程图
```
    HTTP请求
        ↓
 DispatcherServlet
        ↓
   HandlerMapping
        ↓
   HandlerAdapter
        ↓
    Controller
        ↓
   ViewResolver
        ↓
    视图渲染
        ↓
    HTTP响应
```

### 3.2 详细步骤
1. **请求到达**
   - 客户端发送HTTP请求
   - DispatcherServlet接收所有请求

2. **处理器映射**
   - DispatcherServlet调用HandlerMapping
   - HandlerMapping根据URL找到对应的Handler

3. **处理器适配**
   - DispatcherServlet调用HandlerAdapter
   - HandlerAdapter调用具体的Handler处理请求

4. **业务处理**
   - Handler（Controller）执行业务逻辑
   - 返回ModelAndView对象

5. **视图解析**
   - ViewResolver解析ModelAndView中的视图名
   - 找到对应的View对象

6. **视图渲染**
   - View对象渲染数据
   - 生成最终的响应结果

7. **响应返回**
   - 将渲染后的响应返回给客户端

## 四、主要注解说明

### 4.1 @Controller
- 标识一个类是Spring MVC的控制器
- 处理HTTP请求
- 通常与@RequestMapping配合使用

### 4.2 @RequestMapping
- 映射HTTP请求到控制器方法
- 可配置属性：
  - value：指定URL
  - method：指定请求方法
  - params：指定请求参数
  - headers：指定请求头

### 4.3 @ResponseBody
- 将返回值序列化到响应体
- 通常用于返回JSON或XML数据
- RESTful API的重要组成部分

### 4.4 @RequestParam
- 将请求参数绑定到方法参数
- 可配置是否必须
- 可设置默认值

## 五、配置说明

### 5.1 web.xml配置
```xml
<servlet>
    <servlet-name>springmvc</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <load-on-startup>1</load-on-startup>
</servlet>

<servlet-mapping>
    <servlet-name>springmvc</servlet-name>
    <url-pattern>/</url-pattern>
</servlet-mapping>
```

### 5.2 Spring MVC配置
```xml
<!-- 视图解析器配置 -->
<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
    <property name="prefix" value="/WEB-INF/views/"/>
    <property name="suffix" value=".jsp"/>
</bean>

<!-- 注解驱动 -->
<mvc:annotation-driven/>

<!-- 组件扫描 -->
<context:component-scan base-package="com.example.controller"/>
```

## 六、最佳实践

### 6.1 控制器设计
- 遵循单一职责原则
- 保持控制器方法的简洁
- 正确使用HTTP方法
- 合理处理异常

### 6.2 URL设计
- 使用清晰的命名约定
- 遵循RESTful风格
- 合理使用URL参数
- 版本控制考虑

### 6.3 视图解析
- 统一的视图前缀后缀
- 合理的视图组织结构
- 视图复用考虑
- 错误页面处理 