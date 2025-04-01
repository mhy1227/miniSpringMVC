# IDEA 开发环境配置指南

## 一、项目导入

### 1.1 导入项目
1. 打开IDEA
2. File -> Open -> 选择项目目录 `MiniSpringMVC`
3. 等待Maven依赖下载完成
4. 确认项目使用JDK 8

### 1.2 Maven配置
1. File -> Settings -> Build,Execution,Deployment -> Build Tools -> Maven
2. 配置Maven路径（如已配置可跳过）：
   - Maven home path: 选择Maven安装目录
   - User settings file: 选择settings.xml位置
   - Local repository: 选择本地仓库位置

## 二、Tomcat配置

### 2.1 添加Tomcat服务器
1. Run -> Edit Configurations
2. 点击"+" -> Tomcat Server -> Local
3. 配置Server选项卡：
   ```
   Name: MiniSpringMVC
   Application server: 选择Tomcat安装目录
   JRE: 选择JDK 8
   ```

### 2.2 部署配置
1. 切换到Deployment选项卡
2. 点击"+" -> Artifact -> 选择 `mini-spring-mvc:war exploded`
3. Application context 设置为: `/mini-spring-mvc`
4. 端口配置（HTTP port）: `8080`

### 2.3 启动参数配置
1. 在Server选项卡中，点击 VM options
2. 添加以下参数（按需配置）：
   ```
   -Xms512m
   -Xmx1024m
   -XX:MaxPermSize=256m
   ```

## 三、调试配置

### 3.1 断点调试设置
1. 在代码行号处点击设置断点
2. 使用Debug模式启动项目（Shift + F9）
3. Debug窗口查看变量和调用栈

### 3.2 热部署配置
1. Settings -> Build,Execution,Deployment -> Compiler
2. 勾选 "Build project automatically"
3. Settings -> Advanced Settings
4. 勾选 "Allow auto-make to start even..."

## 四、常用快捷键
- Shift + F9: Debug模式启动
- Shift + F10: 运行项目
- Ctrl + F9: 构建项目
- Ctrl + Shift + F9: 热部署
- F8: 断点调试时单步执行
- F9: 断点调试时继续执行

## 五、项目运行

### 5.1 首次运行
1. 确保Tomcat配置正确
2. 点击运行按钮或Debug按钮
3. 等待项目部署完成
4. 访问 `http://localhost:8080/mini-spring-mvc`

### 5.2 常见问题处理
1. 端口占用：
   - 检查8080端口是否被占用
   - 可以在Server配置中修改端口

2. 404错误：
   - 检查Application context是否配置正确
   - 检查web.xml中的url-pattern配置

3. 部署失败：
   - 检查Project Structure中Artifacts配置
   - 清理Tomcat的work目录
   - 重新构建项目

## 六、版本控制集成

### 6.1 Git配置
1. Settings -> Version Control -> Git
2. 配置Git可执行文件路径
3. 确认项目的.git目录被正确识别

### 6.2 忽略文件配置
确保以下文件被忽略：
```
.idea/
*.iml
target/
out/
```

## 七、推荐插件
1. Lombok：简化实体类开发
2. Spring Assistant：Spring配置提示
3. Maven Helper：Maven依赖管理
4. JRebel：热部署增强（可选） 