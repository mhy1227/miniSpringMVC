# ViewResolver 设计文档

## 一、功能概述
ViewResolver 负责将逻辑视图名称解析为具体的视图对象，是 MVC 框架中视图解析的核心组件。

## 二、设计目标
1. 支持 JSP 视图解析
2. 支持视图前缀和后缀配置
3. 提供统一的视图解析接口
4. 可扩展性设计，支持后续添加其他视图类型

## 三、核心接口设计
### 3.1 View 接口
```java
public interface View {
    void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
```

### 3.2 ViewResolver 接口
```java
public interface ViewResolver {
    View resolveViewName(String viewName) throws Exception;
}
```

## 四、实现步骤
1. 创建 View 接口
2. 创建 ViewResolver 接口
3. 实现 JspView 类
4. 实现 InternalResourceViewResolver 类
5. 在 DispatcherServlet 中集成 ViewResolver

## 五、关键类说明
### 5.1 JspView
- 功能：JSP 页面的视图实现
- 职责：渲染 JSP 页面，处理模型数据

### 5.2 InternalResourceViewResolver
- 功能：内部资源视图解析器
- 职责：解析视图名称，支持前缀后缀配置

## 六、配置示例
```properties
# application.properties
mvc.view.prefix=/WEB-INF/views/
mvc.view.suffix=.jsp
```

## 七、使用示例
```java
@Controller
public class HomeController {
    @RequestMapping("/home")
    public String home(Model model) {
        model.addAttribute("message", "Hello World");
        return "home"; // 解析为 /WEB-INF/views/home.jsp
    }
}
```

## 八、扩展点
1. 支持其他视图类型（如 Thymeleaf）
2. 支持视图解析器链
3. 支持视图缓存

## 九、注意事项
1. 视图路径安全性考虑
2. 视图缓存策略
3. 错误处理机制

## 十、实现状态
### 10.1 已完成功能
- [x] View 接口设计与实现
- [x] ViewResolver 接口设计与实现
- [x] JspView 基础实现
- [x] InternalResourceViewResolver 基础实现
- [x] DispatcherServlet 集成

### 10.2 待实现功能
- [ ] Model 数据处理优化
- [ ] JSON 响应支持
- [ ] 视图缓存机制
- [ ] 错误处理完善
- [ ] 其他视图类型支持（如 Thymeleaf） 