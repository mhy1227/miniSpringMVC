# MVC 架构概念详解

## 一、MVC 基本概念

### 1.1 三层架构
1. **Model（模型层）**
   - 负责数据处理和业务逻辑
   - 包括数据访问（DAO）和业务服务（Service）
   - 与数据库交互，执行业务规则

2. **View（视图层）**
   - 负责展示数据给用户
   - 在本项目中主要是 JSP 页面
   - 只负责展示，不包含业务逻辑

3. **Controller（控制器层）**
   - 负责接收用户请求
   - 调用 Model 层处理业务
   - 选择合适的 View 进行展示

### 1.2 各层职责
1. **Controller 层职责**
   - 接收和验证用户输入
   - 将用户输入转换为模型数据
   - 调用业务层处理数据
   - 选择合适的视图展示结果

2. **Model 层职责**
   - Service：处理业务逻辑
   - DAO：数据库访问操作
   - Entity：数据实体对象

3. **View 层职责**
   - 展示数据给用户
   - 提供用户交互界面
   - 不包含业务逻辑代码

## 二、请求处理流程

### 2.1 基本流程
```
浏览器 ---> DispatcherServlet ---> Controller ---> Model ---> View ---> 浏览器
```

### 2.2 详细步骤
1. 用户发送请求（如：/hello.do?name=张三）
2. DispatcherServlet 接收请求
3. HandlerMapping 找到对应的 Controller
4. Controller 处理请求，调用 Service
5. Service 处理业务逻辑，调用 DAO
6. DAO 操作数据库
7. 数据通过 Model 返回
8. Controller 选择视图
9. View 渲染数据
10. 响应返回给用户

## 三、最佳实践

### 3.1 职责分离
1. Controller 只负责：
   - 参数接收和验证
   - 调用 Service
   - 处理响应

2. Service 只负责：
   - 业务逻辑处理
   - 事务管理
   - 调用 DAO

3. DAO 只负责：
   - 数据库操作
   - SQL 执行
   - 结果映射

### 3.2 数据传递
1. Controller 和 View 之间：
   - 使用 Model 传递数据
   - 避免使用 request 属性

2. Controller 和 Service 之间：
   - 使用实体类或 DTO 传递数据
   - 避免使用 Map 等非类型安全的容器

3. Service 和 DAO 之间：
   - 使用实体类传递数据
   - 避免直接传递原始数据类型 