# Mini Spring MVC Framework

一个轻量级的Spring MVC框架实现，用于学习Spring MVC的核心原理。

## 功能特性

- [x] 基于注解的控制器支持
- [x] 依赖注入
- [x] 请求映射
- [x] 视图解析
- [x] 参数绑定
- [x] 返回值处理

## 快速开始

### 环境要求

- JDK 8+
- Maven 3.6+
- Tomcat 8+

### 使用步骤

1. 克隆项目
```bash
git clone https://github.com/mhy1227/mini-spring-mvc.git
```

2. 编译打包
```bash
mvn clean package
```

3. 部署到Tomcat

4. 访问示例应用
```
http://localhost:8080/mini-spring-mvc/
```

## 核心功能说明

### 1. 注解支持
- @Controller
- @Service
- @Autowired
- @RequestMapping

### 2. IoC容器
- 依赖注入
- bean生命周期管理

### 3. MVC功能
- 请求分发
- 参数绑定
- 视图解析
- 返回值处理

## 项目结构
```
mini-spring-mvc/
├── src/
│   ├── main/
│   │   ├── java/        # 源代码
│   │   ├── resources/   # 配置文件
│   │   └── webapp/      # Web资源
│   └── test/            # 测试代码
├── docs/                # 文档
└── pom.xml             # 项目配置
```

## 开发规范
请参考 `docs/development-guide.md`

## 测试
```bash
mvn test
```

## 贡献指南
1. Fork 项目
2. 创建特性分支
3. 提交代码
4. 发起Pull Request

## 许可证
MIT License 
