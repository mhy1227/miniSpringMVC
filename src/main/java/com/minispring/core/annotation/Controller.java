package com.minispring.core.annotation;

import java.lang.annotation.*;

/**
 * 标记一个类为控制器
 * 被此注解标记的类将被视为MVC中的控制器，用于处理HTTP请求
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Controller {
    /**
     * 控制器的名称，默认为空字符串
     */
    String value() default "";
} 