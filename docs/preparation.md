# MiniSpring MVC 项目准备工作

## 一、开发环境准备

### 1.1 基础环境
- JDK 8+
- Maven 3.6+
- IDE (推荐 IntelliJ IDEA)
- Git (版本管理)
- Tomcat 8+

### 1.2 IDE配置
- 安装 Lombok 插件
- 配置 Maven 设置
- 配置 Git 
- 配置 Tomcat

## 二、项目初始化

### 2.1 创建Maven项目
1. **项目信息**
   ```xml
   <groupId>com.minispring</groupId>
   <artifactId>mini-spring-mvc</artifactId>
   <version>1.0-SNAPSHOT</version>
   <packaging>war</packaging>
   ```

2. **项目结构**
   ```
   mini-spring-mvc/
   ├── src/
   │   ├── main/
   │   │   ├── java/
   │   │   ├── resources/
   │   │   └── webapp/
   │   │       └── WEB-INF/
   │   └── test/
   ├── pom.xml
   └── docs/
   ```

### 2.2 配置文件准备

1. **web.xml基础配置**
   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
            http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
            version="4.0">
       
       <servlet>
           <servlet-name>miniSpringMVC</servlet-name>
           <servlet-class>com.minispring.core.DispatcherServlet</servlet-class>
           <load-on-startup>1</load-on-startup>
       </servlet>
       
       <servlet-mapping>
           <servlet-name>miniSpringMVC</servlet-name>
           <url-pattern>/*</url-pattern>
       </servlet-mapping>
       
   </web-app>
   ```

2. **配置文件目录**
   ```
   src/main/resources/
   └── application.properties    # 框架配置文件
   ```

## 三、依赖管理

### 3.1 基础依赖
```xml
<dependencies>
    <!-- Servlet API -->
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>javax.servlet-api</artifactId>
        <version>4.0.1</version>
        <scope>provided</scope>
    </dependency>
    
    <!-- 单元测试 -->
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.13.2</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## 四、开发规范设置

### 4.1 代码规范
- 使用UTF-8编码
- 缩进使用4个空格
- 类名采用大驼峰命名
- 方法名采用小驼峰命名
- 常量全大写，下划线分隔

### 4.2 注释规范
- 类必须有文档注释
- 公共方法必须有文档注释
- 关键代码必须有行注释

### 4.3 Git规范
- 提交信息格式：`type: subject`
- type类型：
  - feat: 新功能
  - fix: 修复bug
  - docs: 文档更新
  - style: 代码格式调整
  - refactor: 重构
  - test: 测试相关

## 五、测试环境准备

### 5.1 单元测试
- 创建测试目录结构
- 准备测试用例模板
- 配置测试覆盖率工具

### 5.2 集成测试
- 配置测试服务器
- 准备测试数据
- 设置测试环境变量

## 六、下一步工作

1. 创建核心包结构
2. 实现基础注解
3. 开发DispatcherServlet
4. 实现IoC容器

准备好这些基础工作后，我们就可以开始具体的框架实现了。需要我详细解释某个部分吗？