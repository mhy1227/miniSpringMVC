# MVC 架构详解

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

## 三、代码示例

### 3.1 实体类（Entity）
```java
public class User {
    private Long id;
    private String name;
    private int age;
    
    // getter 和 setter 方法
}
```

### 3.2 DAO 层
```java
@Repository
public class UserDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public List<User> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM users",
            (rs, rowNum) -> {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                user.setAge(rs.getInt("age"));
                return user;
            }
        );
    }
    
    public void save(User user) {
        jdbcTemplate.update(
            "INSERT INTO users (name, age) VALUES (?, ?)",
            user.getName(),
            user.getAge()
        );
    }
}
```

### 3.3 Service 层
```java
@Service
public class UserService {
    @Autowired
    private UserDao userDao;
    
    public List<User> getAllUsers() {
        return userDao.findAll();
    }
    
    public void saveUser(User user) {
        // 业务逻辑验证
        if (user.getAge() < 0) {
            throw new IllegalArgumentException("年龄不能为负数");
        }
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("姓名不能为空");
        }
        
        userDao.save(user);
    }
}
```

### 3.4 Controller 层
```java
@Controller
public class UserController {
    @Autowired
    private UserService userService;
    
    // 显示用户列表
    @RequestMapping("/users.do")
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "user/list";
    }
    
    // 显示添加用户表单
    @RequestMapping("/user/add.do")
    public String showAddForm() {
        return "user/add";
    }
    
    // 处理添加用户请求
    @RequestMapping("/user/save.do")
    public String addUser(
            @RequestParam String name,
            @RequestParam int age,
            Model model) {
        try {
            User user = new User();
            user.setName(name);
            user.setAge(age);
            
            userService.saveUser(user);
            return "redirect:/users.do";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "user/add";
        }
    }
}
```

### 3.5 View 层
```jsp
<!-- /WEB-INF/views/user/list.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>用户列表</title>
</head>
<body>
    <h1>用户列表</h1>
    
    <table border="1">
        <tr>
            <th>ID</th>
            <th>姓名</th>
            <th>年龄</th>
        </tr>
        <c:forEach items="${users}" var="user">
            <tr>
                <td>${user.id}</td>
                <td>${user.name}</td>
                <td>${user.age}</td>
            </tr>
        </c:forEach>
    </table>
    
    <a href="/user/add.do">添加用户</a>
</body>
</html>

<!-- /WEB-INF/views/user/add.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>添加用户</title>
</head>
<body>
    <h1>添加用户</h1>
    
    <c:if test="${not empty error}">
        <div style="color: red;">${error}</div>
    </c:if>
    
    <form action="/user/save.do" method="post">
        姓名：<input type="text" name="name" required><br>
        年龄：<input type="number" name="age" required><br>
        <input type="submit" value="保存">
    </form>
</body>
</html>
```

## 四、数据流转说明

### 4.1 添加用户流程
1. 用户访问 `/user/add.do`
2. Controller 返回 `add.jsp` 视图
3. 用户填写表单并提交到 `/user/save.do`
4. Controller 接收参数并创建 User 对象
5. 调用 Service 保存用户
6. Service 进行业务验证
7. 验证通过后调用 DAO 保存数据
8. 重定向到用户列表页面

### 4.2 查看用户列表流程
1. 用户访问 `/users.do`
2. Controller 调用 Service 获取用户列表
3. Service 调用 DAO 查询数据库
4. 数据通过 Model 传递给视图
5. JSP 页面遍历数据并展示

## 五、最佳实践

### 5.1 职责分离
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

### 5.2 数据传递
1. Controller 和 View 之间：
   - 使用 Model 传递数据
   - 避免使用 request 属性

2. Controller 和 Service 之间：
   - 使用实体类或 DTO 传递数据
   - 避免使用 Map 等非类型安全的容器

3. Service 和 DAO 之间：
   - 使用实体类传递数据
   - 避免直接传递原始数据类型

### 5.3 异常处理
1. DAO 层：
   - 抛出具体的数据访问异常
   - 包装 SQL 异常

2. Service 层：
   - 处理业务异常
   - 转换数据访问异常为业务异常

3. Controller 层：
   - 处理用户输入异常
   - 统一异常处理
   - 友好的错误提示 