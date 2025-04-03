# MVC 架构简明教程

## 一、什么是 MVC？

MVC 是一种软件架构模式，将应用程序分为三个核心部分：
1. Model（模型）：处理数据和业务逻辑
2. View（视图）：负责数据的展示
3. Controller（控制器）：处理用户请求，协调 Model 和 View

## 二、基本工作流程

1. 用户发送请求：
```
http://localhost:8080/hello.do?name=张三
```

2. 请求处理流程：
```
用户请求 -> DispatcherServlet -> Controller -> Service -> DAO -> 数据库
响应返回 <- 视图(JSP) <- Controller <- Service <- DAO <- 数据库
```

## 三、代码示例

1. Controller 层（处理请求）：
```java
@Controller
public class UserController {
    @Autowired
    private UserService userService;
    
    @RequestMapping("/hello.do")
    public String hello(@RequestParam String name, Model model) {
        String message = userService.sayHello(name);
        model.addAttribute("message", message);
        return "hello";
    }
}
```

2. Service 层（业务逻辑）：
```java
@Service
public class UserService {
    public String sayHello(String name) {
        return "Hello, " + name + "!";
    }
}
```

3. View 层（展示）：
```jsp
<html>
<body>
    <h1>${message}</h1>
</body>
</html>
```

## 四、关键点说明

1. Controller 职责：
   - 接收请求参数
   - 调用业务服务
   - 返回视图

2. Service 职责：
   - 处理业务逻辑
   - 数据校验
   - 调用数据访问

3. View 职责：
   - 展示数据
   - 用户界面
   - 不含业务逻辑 