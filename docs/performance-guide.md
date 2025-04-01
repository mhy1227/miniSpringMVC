# MiniSpring MVC 性能优化指南

## 一、性能测试结果

### 1.1 基准测试
- 测试环境：4核8G
- JDK版本：8u321
- 测试工具：JMeter 5.4.3

#### 测试结果
```
并发用户数：100
测试时长：10分钟
平均响应时间：150ms
TPS：500
错误率：0.01%
```

### 1.2 性能瓶颈
1. IoC容器初始化慢
2. 反射调用开销大
3. 视图解析耗时
4. 数据库连接池配置不合理

## 二、优化建议

### 2.1 IoC容器优化

#### 1. Bean实例缓存
```java
public class BeanFactory {
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>();
    
    public Object getBean(String name) {
        // 先从缓存中获取
        Object bean = singletonObjects.get(name);
        if (bean != null) {
            return bean;
        }
        // 创建Bean并缓存
        bean = createBean(name);
        singletonObjects.put(name, bean);
        return bean;
    }
}
```

#### 2. 注解信息缓存
```java
public class AnnotationMetadataCache {
    private static final Map<Class<?>, Annotation[]> annotationCache = new ConcurrentHashMap<>();
    
    public static Annotation[] getAnnotations(Class<?> clazz) {
        return annotationCache.computeIfAbsent(clazz, Class::getAnnotations);
    }
}
```

### 2.2 反射优化

#### 1. Method缓存
```java
public class ReflectionCache {
    private static final Map<Class<?>, Method[]> methodCache = new ConcurrentHashMap<>();
    
    public static Method[] getMethods(Class<?> clazz) {
        return methodCache.computeIfAbsent(clazz, Class::getDeclaredMethods);
    }
}
```

#### 2. 构造函数优化
```java
public class BeanCreator {
    private static final Map<Class<?>, Constructor<?>> constructorCache = new ConcurrentHashMap<>();
    
    public static Object newInstance(Class<?> clazz) {
        Constructor<?> constructor = constructorCache.computeIfAbsent(clazz, 
            c -> {
                try {
                    Constructor<?> ctor = c.getDeclaredConstructor();
                    ctor.setAccessible(true);
                    return ctor;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        try {
            return constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
```

### 2.3 视图解析优化

#### 1. 视图缓存
```java
public class ViewResolver {
    private final Map<String, View> viewCache = new ConcurrentHashMap<>();
    
    public View resolveViewName(String viewName) {
        return viewCache.computeIfAbsent(viewName, this::createView);
    }
}
```

#### 2. 模板预编译
```java
public class TemplateCompiler {
    private final Map<String, CompiledTemplate> templateCache = new ConcurrentHashMap<>();
    
    public CompiledTemplate getTemplate(String templatePath) {
        return templateCache.computeIfAbsent(templatePath, this::compile);
    }
}
```

### 2.4 数据库优化

#### 1. 连接池配置
```properties
# HikariCP配置
hikari.maximumPoolSize=20
hikari.minimumIdle=5
hikari.idleTimeout=300000
hikari.connectionTimeout=20000
hikari.maxLifetime=1200000
```

#### 2. SQL优化
```java
public class UserDao {
    // 使用批量操作
    public void batchInsert(List<User> users) {
        String sql = "INSERT INTO user (name, age) VALUES (?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                User user = users.get(i);
                ps.setString(1, user.getName());
                ps.setInt(2, user.getAge());
            }
            
            @Override
            public int getBatchSize() {
                return users.size();
            }
        });
    }
}
```

## 三、JVM调优

### 3.1 JVM参数配置
```bash
JAVA_OPTS="
    -Xms2g
    -Xmx2g
    -XX:MetaspaceSize=256m
    -XX:MaxMetaspaceSize=512m
    -XX:+UseG1GC
    -XX:MaxGCPauseMillis=200
    -XX:+HeapDumpOnOutOfMemoryError
    -XX:HeapDumpPath=/path/to/dump
    -XX:+PrintGCDetails
    -XX:+PrintGCDateStamps
    -Xloggc:/path/to/gc.log
"
```

### 3.2 GC优化
1. 选择合适的GC
   - G1GC：大内存、低延迟
   - CMS：中等内存、低延迟
   - ParallelGC：高吞吐量

2. GC参数调优
```bash
# G1GC参数
-XX:+UseG1GC
-XX:MaxGCPauseMillis=200
-XX:G1HeapRegionSize=8m
-XX:InitiatingHeapOccupancyPercent=45
```

## 四、监控方案

### 4.1 JVM监控
```java
@Configuration
public class MonitorConfig {
    @Bean
    public MBeanServer mBeanServer() {
        return ManagementFactory.getPlatformMBeanServer();
    }
    
    @Bean
    public JmxMBeanServer jmxMBeanServer() {
        return new JmxMBeanServer(mBeanServer());
    }
}
```

### 4.2 性能指标
```java
@Component
public class PerformanceMonitor {
    private final Counter requestCounter;
    private final Timer responseTimer;
    
    public PerformanceMonitor(MeterRegistry registry) {
        this.requestCounter = registry.counter("http.requests");
        this.responseTimer = registry.timer("http.response.time");
    }
    
    public void recordRequest() {
        requestCounter.increment();
    }
    
    public void recordResponseTime(long timeMs) {
        responseTimer.record(timeMs, TimeUnit.MILLISECONDS);
    }
}
```

## 五、最佳实践

### 5.1 编码最佳实践

1. 使用StringBuilder
```java
public String buildString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 1000; i++) {
        sb.append("item").append(i);
    }
    return sb.toString();
}
```

2. 避免创建不必要的对象
```java
// 不推荐
String s = new String("hello");

// 推荐
String s = "hello";
```

### 5.2 并发最佳实践

1. 使用线程池
```java
@Configuration
public class ThreadPoolConfig {
    @Bean
    public ExecutorService executorService() {
        return new ThreadPoolExecutor(
            10,                 // 核心线程数
            20,                // 最大线程数
            60L,               // 空闲线程存活时间
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(100)  // 工作队列
        );
    }
}
```

2. 避免锁竞争
```java
public class LockExample {
    private final Map<String, Lock> lockMap = new ConcurrentHashMap<>();
    
    public void processWithLock(String key) {
        Lock lock = lockMap.computeIfAbsent(key, k -> new ReentrantLock());
        lock.lock();
        try {
            // 处理逻辑
        } finally {
            lock.unlock();
        }
    }
}
```

### 5.3 缓存最佳实践

1. 多级缓存
```java
public class MultiLevelCache {
    private final Cache<String, Object> localCache;  // 本地缓存
    private final Cache<String, Object> redisCache;  // 分布式缓存
    
    public Object get(String key) {
        // 先查本地缓存
        Object value = localCache.get(key);
        if (value != null) {
            return value;
        }
        
        // 再查分布式缓存
        value = redisCache.get(key);
        if (value != null) {
            localCache.put(key, value);  // 回填本地缓存
        }
        return value;
    }
}
```

2. 缓存预热
```java
@Component
public class CacheWarmer implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private Cache cache;
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 加载热点数据到缓存
        loadHotData();
    }
} 