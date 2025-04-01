# MiniSpring MVC 开发规范指南

## 一、代码规范

### 1.1 命名规范

#### 1.1.1 包命名
- 全部小写
- 单词间不使用分隔符
- 包名统一使用单数形式
- 例如：`com.minispring.core`

#### 1.1.2 类命名
- 使用大驼峰命名法（PascalCase）
- 类名应该是名词或名词短语
- 接口名可以是形容词或形容词短语
- 测试类以Test结尾
- 例如：
  ```java
  public class UserController {}
  public interface Configurable {}
  public class UserServiceTest {}
  ```

#### 1.1.3 方法命名
- 使用小驼峰命名法（camelCase）
- 方法名应该是动词或动词短语
- 例如：
  ```java
  public void getUserById() {}
  public void saveUser() {}
  ```

#### 1.1.4 变量命名
- 使用小驼峰命名法
- 变量名应该是名词
- 布尔类型变量使用is/has/can等前缀
- 例如：
  ```java
  private String userName;
  private boolean isValid;
  private List<String> userList;
  ```

#### 1.1.5 常量命名
- 全部大写
- 单词间用下划线分隔
- 例如：
  ```java
  public static final String DEFAULT_PAGE_SIZE = "10";
  ```

### 1.2 格式规范

#### 1.2.1 缩进
- 使用4个空格进行缩进
- 不使用Tab字符

#### 1.2.2 行宽
- 每行最多120个字符
- 超过限制时进行换行

#### 1.2.3 空行
- 类的成员之间加空行
- 方法之间加空行
- 方法内逻辑块之间加空行

#### 1.2.4 括号
- 左大括号不换行
- 右大括号独占一行
- if/for等语句必须使用大括号

### 1.3 注释规范

#### 1.3.1 类注释
```java
/**
 * 类的功能描述
 *
 * @author 作者
 * @version 版本
 * @since 起始版本
 */
```

#### 1.3.2 方法注释
```java
/**
 * 方法的功能描述
 *
 * @param 参数名 参数说明
 * @return 返回值说明
 * @throws 异常类型 异常说明
 */
```

#### 1.3.3 字段注释
```java
/** 字段说明 */
private String field;
```

## 二、项目规范

### 2.1 包结构
```
com.minispring
├── annotation    // 注解定义
├── core         // 核心功能
├── context      // 上下文相关
├── web          // Web相关
├── util         // 工具类
└── example      // 示例代码
```

### 2.2 异常处理
- 优先使用Java标准异常
- 自定义异常应继承RuntimeException
- 异常命名以Exception结尾
- 提供清晰的异常信息

### 2.3 日志规范
- 使用SLF4J + Logback
- 统一日志格式
- 合理的日志级别
- 敏感信息不打印

## 三、Git规范

### 3.1 分支管理
- master：主分支，稳定版本
- develop：开发分支
- feature/*：特性分支
- bugfix/*：bug修复分支
- release/*：发布分支

### 3.2 提交信息
```
<type>(<scope>): <subject>

<body>

<footer>
```

- type类型：
  - feat：新功能
  - fix：修复bug
  - docs：文档更新
  - style：代码格式
  - refactor：重构
  - test：测试相关
  - chore：构建过程或辅助工具的变动

### 3.3 版本规范
- 遵循语义化版本
- 格式：主版本号.次版本号.修订号
- 例如：1.0.0

## 四、测试规范

### 4.1 单元测试
- 测试类名：被测试类名 + Test
- 测试方法名：test + 方法名 + 场景
- 每个测试方法只测试一个场景
- 使用断言进行验证

### 4.2 测试覆盖率
- 分支覆盖率 > 80%
- 行覆盖率 > 85%
- 方法覆盖率 > 90%

## 五、文档规范

### 5.1 文档结构
```
docs/
├── api/          // API文档
├── design/       // 设计文档
├── guide/        // 使用指南
└── test/         // 测试文档
```

### 5.2 文档格式
- 使用Markdown格式
- 标题使用层级结构
- 代码块指定语言
- 图表使用PlantUML

## 六、安全规范

### 6.1 代码安全
- 避免硬编码敏感信息
- 使用安全的加密算法
- 注意SQL注入防护
- 防止XSS攻击

### 6.2 配置安全
- 敏感配置加密
- 生产环境配置隔离
- 权限最小化原则

## 七、性能规范

### 7.1 代码性能
- 避免创建不必要的对象
- 使用StringBuilder拼接字符串
- 合理使用缓存
- 避免过度优化

### 7.2 资源管理
- 及时释放资源
- 使用try-with-resources
- 避免内存泄漏
- 控制日志输出 