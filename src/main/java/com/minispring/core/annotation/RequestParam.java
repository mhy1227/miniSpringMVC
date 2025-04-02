package com.minispring.core.annotation;

import java.lang.annotation.*;

/**
 * 请求参数注解
 * 用于将请求参数绑定到控制器方法的参数上
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {
    /**
     * 参数名称
     */
    String value() default "";
    
    /**
     * 参数是否必须
     */
    boolean required() default true;
    
    /**
     * 默认值
     */
    String defaultValue() default "";
} 