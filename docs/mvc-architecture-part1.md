# MVC 架构详解（第一部分）

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