# MiniSpring MVC 示例应用

## 一、功能说明

### 1.1 用户管理模块
- 用户注册
- 用户登录
- 用户信息管理
- 角色权限管理

### 1.2 文章管理模块
- 文章发布
- 文章编辑
- 文章删除
- 文章评论

### 1.3 文件管理模块
- 文件上传
- 文件下载
- 图片处理

## 二、代码结构

### 2.1 项目结构
```
com.minispring.example
├── controller        // 控制器层
│   ├── UserController.java
│   ├── ArticleController.java
│   └── FileController.java
├── service          // 服务层
│   ├── UserService.java
│   ├── ArticleService.java
│   └── FileService.java
├── repository       // 数据访问层
│   ├── UserRepository.java
│   ├── ArticleRepository.java
│   └── FileRepository.java
├── model           // 数据模型
│   ├── User.java
│   ├── Article.java
│   └── File.java
└── config          // 配置类
    └── AppConfig.java
```

### 2.2 视图结构
```
webapp/
├── WEB-INF/
│   ├── views/
│   │   ├── user/
│   │   │   ├── login.jsp
│   │   │   └── register.jsp
│   │   ├── article/
│   │   │   ├── list.jsp
│   │   │   └── edit.jsp
│   │   └── file/
│   │       ├── upload.jsp
│   │       └── download.jsp
│   └── web.xml
└── static/
    ├── css/
    ├── js/
    └── img/
```

## 三、代码示例

### 3.1 控制器示例

#### UserController
```java
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public Result register(@RequestBody UserDTO userDTO) {
        return userService.register(userDTO);
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Result login(@RequestBody LoginDTO loginDTO) {
        return userService.login(loginDTO);
    }
}
```

### 3.2 服务层示例

#### UserService
```java
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public Result register(UserDTO userDTO) {
        // 参数校验
        if (!validateUserDTO(userDTO)) {
            return Result.fail("参数不合法");
        }
        
        // 业务处理
        User user = convertToUser(userDTO);
        userRepository.save(user);
        
        return Result.success();
    }
}
```

### 3.3 数据访问层示例

#### UserRepository
```java
@Repository
public class UserRepositoryImpl implements UserRepository {
    @Override
    public void save(User user) {
        // 实现保存用户逻辑
    }
    
    @Override
    public User findById(Long id) {
        // 实现查询用户逻辑
        return null;
    }
}
```

## 四、使用说明

### 4.1 环境准备
1. 创建数据库
```sql
CREATE DATABASE minispring;
USE minispring;

CREATE TABLE user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

2. 配置数据源
```properties
jdbc.url=jdbc:mysql://localhost:3306/minispring
jdbc.username=root
jdbc.password=root
```

### 4.2 启动应用
1. 编译打包
```bash
mvn clean package
```

2. 部署运行
```bash
cp target/mini-spring-mvc.war $CATALINA_HOME/webapps/
$CATALINA_HOME/bin/startup.sh
```

### 4.3 接口测试
1. 注册接口
```bash
curl -X POST http://localhost:8080/mini-spring-mvc/user/register \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"123456","email":"test@example.com"}'
```

2. 登录接口
```bash
curl -X POST http://localhost:8080/mini-spring-mvc/user/login \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"123456"}'
```

## 五、最佳实践

### 5.1 控制器最佳实践
1. 统一返回格式
```java
public class Result<T> {
    private int code;
    private String message;
    private T data;
}
```

2. 统一异常处理
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handleException(Exception e) {
        return Result.fail(e.getMessage());
    }
}
```

### 5.2 服务层最佳实践
1. 事务管理
```java
@Service
public class ArticleServiceImpl implements ArticleService {
    @Transactional
    public void createArticle(ArticleDTO articleDTO) {
        // 业务逻辑
    }
}
```

2. 数据校验
```java
public class ValidationUtils {
    public static void validateArticle(ArticleDTO articleDTO) {
        // 校验逻辑
    }
}
```

### 5.3 数据访问层最佳实践
1. 连接池配置
```xml
<bean id="dataSource" class="com.zaxxer.hikari.HikariDataSource">
    <property name="maximumPoolSize" value="10"/>
    <property name="connectionTimeout" value="30000"/>
</bean>
```

2. SQL优化
```java
@Repository
public class ArticleRepositoryImpl implements ArticleRepository {
    public List<Article> findByCondition(ArticleQuery query) {
        // 使用索引优化查询
    }
}
```

## 六、扩展示例

### 6.1 文件上传
```java
@Controller
@RequestMapping("/file")
public class FileController {
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public Result upload(@RequestParam("file") MultipartFile file) {
        // 文件上传处理
    }
}
```

### 6.2 权限控制
```java
@Aspect
@Component
public class SecurityAspect {
    @Before("@annotation(requiresAuth)")
    public void checkAuth(RequiresAuth requiresAuth) {
        // 权限检查逻辑
    }
}
```

### 6.3 缓存使用
```java
@Service
public class ArticleServiceImpl implements ArticleService {
    @Cacheable(value = "articles", key = "#id")
    public Article getArticle(Long id) {
        // 查询文章
    }
}
``` 