# MVC 实战案例：用户管理系统

## 一、功能需求

实现一个简单的用户管理系统，包含：
1. 用户列表展示
2. 添加新用户
3. 查看用户详情

## 二、代码实现

### 1. 实体类
```java
public class User {
    private Long id;
    private String name;
    private int age;
    private String email;
    
    // getter 和 setter
}
```

### 2. DAO 层
```java
@Repository
public class UserDao {
    private JdbcTemplate jdbcTemplate;
    
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }
    
    public User findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new UserRowMapper(), id);
    }
    
    public void save(User user) {
        String sql = "INSERT INTO users (name, age, email) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, user.getName(), user.getAge(), user.getEmail());
    }
}
```

### 3. Service 层
```java
@Service
public class UserService {
    @Autowired
    private UserDao userDao;
    
    public List<User> getAllUsers() {
        return userDao.findAll();
    }
    
    public User getUserById(Long id) {
        return userDao.findById(id);
    }
    
    public void createUser(User user) {
        // 业务验证
        validateUser(user);
        // 保存用户
        userDao.save(user);
    }
    
    private void validateUser(User user) {
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (user.getAge() < 0 || user.getAge() > 150) {
            throw new IllegalArgumentException("年龄必须在 0-150 之间");
        }
        if (user.getEmail() != null && !user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("邮箱格式不正确");
        }
    }
}
```

### 4. Controller 层
```java
@Controller
public class UserController {
    @Autowired
    private UserService userService;
    
    // 用户列表
    @RequestMapping("/users.do")
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "user/list";
    }
    
    // 用户详情
    @RequestMapping("/user/view.do")
    public String viewUser(@RequestParam Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "user/view";
    }
    
    // 添加用户表单
    @RequestMapping("/user/add.do")
    public String showAddForm() {
        return "user/add";
    }
    
    // 保存用户
    @RequestMapping("/user/save.do")
    public String saveUser(
            @RequestParam String name,
            @RequestParam int age,
            @RequestParam(required = false) String email,
            Model model) {
        try {
            User user = new User();
            user.setName(name);
            user.setAge(age);
            user.setEmail(email);
            
            userService.createUser(user);
            return "redirect:/users.do";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "user/add";
        }
    }
}
```

### 5. View 层

用户列表页面（list.jsp）：
```jsp
<%@ page contentType="text/html;charset=UTF-8" %>
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
            <th>邮箱</th>
            <th>操作</th>
        </tr>
        <c:forEach items="${users}" var="user">
            <tr>
                <td>${user.id}</td>
                <td>${user.name}</td>
                <td>${user.age}</td>
                <td>${user.email}</td>
                <td>
                    <a href="/user/view.do?id=${user.id}">查看</a>
                </td>
            </tr>
        </c:forEach>
    </table>
    
    <a href="/user/add.do">添加用户</a>
</body>
</html>
```

添加用户页面（add.jsp）：
```jsp
<%@ page contentType="text/html;charset=UTF-8" %>
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
        邮箱：<input type="email" name="email"><br>
        <input type="submit" value="保存">
    </form>
    
    <a href="/users.do">返回列表</a>
</body>
</html>
```

## 三、流程说明

1. 查看用户列表：
   - 访问 `/users.do`
   - Controller 调用 Service 获取所有用户
   - Service 调用 DAO 查询数据库
   - 数据通过 Model 传递给视图
   - 视图展示用户列表

2. 添加新用户：
   - 访问 `/user/add.do` 显示表单
   - 用户填写表单并提交到 `/user/save.do`
   - Controller 接收参数并创建 User 对象
   - Service 进行数据验证
   - 验证通过后 DAO 保存到数据库
   - 重定向到用户列表页面

3. 查看用户详情：
   - 点击用户列表中的"查看"链接
   - 访问 `/user/view.do?id=xx`
   - Controller 调用 Service 获取用户信息
   - Service 调用 DAO 查询指定用户
   - 数据通过 Model 传递给视图
   - 视图展示用户详情 