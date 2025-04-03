package com.minispring.core.annotation;

import java.lang.annotation.*;

/**
 * 控制器注解
 * 标记一个类为Web控制器
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