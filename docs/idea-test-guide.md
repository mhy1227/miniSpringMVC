# IDEA 测试环境配置指南

## 一、IDEA配置Tomcat
1. 打开 IDEA 的 Run/Debug Configurations（运行/调试配置）
   - 点击顶部工具栏的 "Add Configuration"
   - 点击 "+" 号，选择 "Tomcat Server" -> "Local"

2. 配置 Tomcat Server
   - Server 选项卡:
     - Application Server: 选择或配置你的 Tomcat 路径
     - URL: `http://localhost:8080/`
     - JMX port: 默认即可
   
   - Deployment 选项卡:
     - 点击 "+" 选择 "Artifact"
     - 选择项目的 war 包（通常是 `项目名:war exploded`）
     - Application context: 设置为 `/` （这是应用的根路径）

3. 配置项目结构
   - 打开 File -> Project Structure
   - 在 Artifacts 中确保有 war exploded 配置
   - 检查 Dependencies 中是否包含所需依赖

## 二、准备测试环境

1. 创建配置文件
```bash
src/main/resources/application.properties
```
添加以下内容：
```properties
scan.package=com.minispring.test
mvc.view.prefix=/WEB-INF/views/
mvc.view.suffix=.jsp
```

2. 创建测试目录结构
```bash
src/main/webapp/WEB-INF/views/    # JSP文件目录
src/test/java/com/minispring/test/mvc/    # 测试控制器目录
```

3. 确保 web.xml 配置正确（位于 src/main/webapp/WEB-INF/web.xml）
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

## 三、运行测试

1. 基本参数测试
   - 启动项目（点击IDEA顶部的绿色运行按钮）
   - 等待Tomcat启动完成
   - 在浏览器访问：`http://localhost:8080/hello`
   - 预期看到：`Hello, World!`

2. 调试技巧
   - 在 DispatcherServlet 的关键方法上设置断点
   - 使用 IDEA 的 Debug 模式启动
   - 查看 IDEA 底部的 Console 日志输出
   - 使用 IDEA 的 HTTP Client 工具测试（可选）

## 四、常见问题解决

1. 404 错误检查清单：
   - Controller 类是否有 @Controller 注解
   - 包名是否在 scan.package 配置范围内
   - URL 是否正确（注意大小写）
   - web.xml 的 url-pattern 是否正确

2. 500 错误检查清单：
   - 查看 IDEA 的 Console 日志
   - 检查参数类型是否匹配
   - 检查 JSP 文件路径是否正确

3. 中文乱码解决：
   - 在 Tomcat 的 VM options 中添加：
     ```
     -Dfile.encoding=UTF-8
     ```
   - 检查 JSP 文件的编码设置

## 五、测试用例执行

1. 参数绑定测试
```http
GET http://localhost:8080/hello
GET http://localhost:8080/hello?name=张三
GET http://localhost:8080/user?id=123
```

2. 视图测试
```http
GET http://localhost:8080/view/test
GET http://localhost:8080/view/test?message=测试消息
```

## 六、IDEA 调试技巧

1. 设置断点位置：
   - DispatcherServlet.service()
   - RequestParamArgumentResolver.resolveArgument()
   - HandlerMapping.getHandler()

2. 使用 IDEA 的 Evaluate Expression：
   - 在断点处查看变量值
   - 执行表达式测试

3. 使用 IDEA 的 HTTP Client：
   - 创建 .http 文件
   - 编写测试请求
   - 直接在 IDEA 中执行请求 