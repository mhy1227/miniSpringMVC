package com.minispring.core.annotation;

import java.lang.annotation.*;

/**
 * 标记URL映射关系
 * 可以用在类和方法上，用于定义HTTP请求映射
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
    /**
     * 请求路径
     */
    String value() default "";
    
    /**
     * 请求方法
     */
    RequestMethod[] method() default {};
    
    /**
     * 请求参数条件
     * 例如: params={"myParam=myValue"}
     */
    String[] params() default {};
    
    /**
     * 请求头条件
     * 例如: headers={"myHeader=myValue"}
     */
    String[] headers() default {};
    
    /**
     * 指定处理请求的提交内容类型(Content-Type)
     * 例如: consumes="application/json"
     */
    String[] consumes() default {};
    
    /**
     * 指定返回的内容类型
     * 例如: produces="application/json"
     */
    String[] produces() default {};
} 