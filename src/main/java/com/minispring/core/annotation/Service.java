package com.minispring.core.annotation;

import java.lang.annotation.*;

/**
 * 标记一个类为服务类
 * 被此注解标记的类将被视为服务层的组件，用于处理业务逻辑
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Service {
    /**
     * 服务的名称，默认为空字符串
     */
    String value() default "";
} 