# MiniSpring MVC 测试指南

## 一、环境准备

1. 确保已配置 Tomcat
2. 创建测试配置文件 `src/main/resources/application.properties`：
```properties
scan.package=com.minispring.test
mvc.view.prefix=/WEB-INF/views/
mvc.view.suffix=.jsp
```

3. 配置 web.xml：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
         http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1">

    <servlet>
        <servlet-name>miniSpring</servlet-name>
        <servlet-class>com.minispring.core.web.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:application.properties</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>

    <servlet-mapping>
        <servlet-name>miniSpring</servlet-name>
        <url-pattern>/*</url-pattern>
    </servlet-mapping>
</web-app>
```

## 二、测试用例

### 1. 基本参数绑定测试
已有的 UserController：
```java
@Controller
public class UserController {
    @RequestMapping("/user")
    public String getUser(@RequestParam("id") Integer id) {
        return "user-" + id;
    }
    
    @RequestMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hello, " + name + "!";
    }
}
```

测试场景：
1. 访问 `http://localhost:8080/hello`
   - 预期结果：`Hello, World!`
   - 测试点：默认值处理

2. 访问 `http://localhost:8080/hello?name=张三`
   - 预期结果：`Hello, 张三!`
   - 测试点：参数正确传递

3. 访问 `http://localhost:8080/user?id=123`
   - 预期结果：`user-123`
   - 测试点：数字类型转换

4. 访问 `http://localhost:8080/user`
   - 预期结果：错误提示（参数缺失）
   - 测试点：必需参数验证

### 2. 视图解析测试

创建视图测试控制器：
```java
@Controller
public class ViewController {
    @RequestMapping("/view/test")
    public String testView(
            @RequestParam(value = "message", defaultValue = "Hello JSP") String message) {
        request.setAttribute("message", message);
        return "test";  // 将解析为 /WEB-INF/views/test.jsp
    }
}
```

创建对应的 JSP 文件 `/WEB-INF/views/test.jsp`：
```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>View Test</title>
</head>
<body>
    <h1>${message}</h1>
</body>
</html>
```

测试场景：
1. 访问 `http://localhost:8080/view/test`
   - 预期结果：显示包含 "Hello JSP" 的页面
   - 测试点：基本视图解析

2. 访问 `http://localhost:8080/view/test?message=自定义消息`
   - 预期结果：显示包含 "自定义消息" 的页面
   - 测试点：参数传递到视图

## 三、测试步骤

1. 部署准备
   - 将项目打包：`mvn clean package`
   - 将 war 包部署到 Tomcat 的 webapps 目录
   - 启动 Tomcat

2. 执行测试
   - 使用浏览器访问上述测试URL
   - 检查返回结果是否符合预期
   - 查看 Tomcat 日志是否有错误信息

3. 常见问题排查
   - 404错误：检查URL映射和Controller注解
   - 500错误：检查参数绑定和类型转换
   - 空白页面：检查视图解析器配置和JSP文件位置

## 四、测试结果记录

建议记录以下信息：
1. 测试场景
2. 实际结果
3. 是否符合预期
4. 发现的问题
5. 解决方案

## 五、注意事项

1. 确保编译时添加了 JSP 支持
2. 中文参数需要注意编码问题
3. 留意控制台的异常信息
4. 测试前确保配置文件正确加载 