# MiniSpring MVC 部署指南

## 一、环境要求

### 1.1 基础环境
- JDK 8+
  - 推荐版本：JDK 8u321
  - 设置JAVA_HOME环境变量
  - 配置PATH环境变量

- Maven 3.6+
  - 推荐版本：Maven 3.6.3
  - 设置MAVEN_HOME环境变量
  - 配置settings.xml

- Tomcat 8+
  - 推荐版本：Tomcat 8.5.78
  - 设置CATALINA_HOME环境变量
  - 配置server.xml

### 1.2 推荐配置
- CPU: 2核+
- 内存: 4GB+
- 磁盘: 20GB+
- 操作系统: Linux/Windows/MacOS

## 二、安装步骤

### 2.1 开发环境部署

1. **克隆项目**
```bash
git clone https://github.com/yourusername/mini-spring-mvc.git
cd mini-spring-mvc
```

2. **编译打包**
```bash
mvn clean package
```

3. **部署到Tomcat**
```bash
cp target/mini-spring-mvc.war $CATALINA_HOME/webapps/
```

4. **启动Tomcat**
```bash
$CATALINA_HOME/bin/startup.sh  # Linux/Mac
%CATALINA_HOME%\bin\startup.bat  # Windows
```

### 2.2 生产环境部署

1. **准备配置文件**
```properties
# application-prod.properties
server.port=8080
server.context-path=/mini-spring-mvc
logging.level.root=INFO
```

2. **配置Tomcat**
```xml
<!-- server.xml -->
<Connector port="8080" protocol="HTTP/1.1"
           connectionTimeout="20000"
           redirectPort="8443" />
```

3. **配置JVM参数**
```bash
export JAVA_OPTS="-Xms2g -Xmx2g -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m"
```

4. **部署应用**
```bash
# 停止Tomcat
$CATALINA_HOME/bin/shutdown.sh

# 部署WAR包
rm -rf $CATALINA_HOME/webapps/mini-spring-mvc*
cp mini-spring-mvc.war $CATALINA_HOME/webapps/

# 启动Tomcat
$CATALINA_HOME/bin/startup.sh
```

## 三、配置说明

### 3.1 核心配置

```properties
# 基础配置
app.name=MiniSpringMVC
app.version=1.0.0
scan.package=com.minispring

# 数据源配置
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/minispring
jdbc.username=root
jdbc.password=root

# MVC配置
mvc.view.prefix=/WEB-INF/views/
mvc.view.suffix=.jsp
```

### 3.2 日志配置

```xml
<!-- logback.xml -->
<configuration>
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <root level="INFO">
        <appender-ref ref="CONSOLE" />
    </root>
</configuration>
```

## 四、验证部署

### 4.1 检查部署状态
1. 访问应用: `http://localhost:8080/mini-spring-mvc`
2. 检查日志: `$CATALINA_HOME/logs/catalina.out`
3. 检查进程: `ps -ef | grep tomcat`

### 4.2 健康检查
```bash
# 检查HTTP状态
curl -I http://localhost:8080/mini-spring-mvc/health

# 检查日志
tail -f $CATALINA_HOME/logs/catalina.out

# 检查JVM状态
jps -mlvV
```

## 五、常见问题

### 5.1 启动问题
1. **端口冲突**
   ```bash
   netstat -ano | findstr "8080"
   taskkill /F /PID <pid>
   ```

2. **内存不足**
   ```bash
   # 修改JVM参数
   export JAVA_OPTS="-Xms1g -Xmx1g"
   ```

3. **权限问题**
   ```bash
   chmod -R 755 $CATALINA_HOME
   chown -R tomcat:tomcat $CATALINA_HOME
   ```

### 5.2 运行问题
1. **404错误**
   - 检查WAR包是否正确部署
   - 检查URL映射是否正确
   - 检查Controller是否正确注册

2. **500错误**
   - 检查日志文件
   - 检查数据库连接
   - 检查配置文件

3. **性能问题**
   - 检查JVM参数
   - 检查数据库连接池
   - 检查线程池配置

## 六、监控方案

### 6.1 JVM监控
```bash
# 使用JMX
-Dcom.sun.management.jmxremote
-Dcom.sun.management.jmxremote.port=9010
-Dcom.sun.management.jmxremote.ssl=false
-Dcom.sun.management.jmxremote.authenticate=false
```

### 6.2 应用监控
- 接入Prometheus
- 配置Grafana面板
- 设置告警规则

## 七、备份方案

### 7.1 配置备份
```bash
# 备份配置文件
tar -czf config_backup_$(date +%Y%m%d).tar.gz conf/

# 备份数据
mysqldump -u root -p minispring > backup_$(date +%Y%m%d).sql
```

### 7.2 应用备份
```bash
# 备份WAR包
cp mini-spring-mvc.war mini-spring-mvc_$(date +%Y%m%d).war

# 备份日志
tar -czf logs_$(date +%Y%m%d).tar.gz logs/
``` 