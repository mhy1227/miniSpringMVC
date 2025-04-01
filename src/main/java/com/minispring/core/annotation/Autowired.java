package com.minispring.core.annotation;

import java.lang.annotation.*;

/**
 * 标记需要进行依赖注入的字段、方法或构造器
 * 被此注解标记的元素将由IoC容器自动注入对应的实例
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
    /**
     * 是否必须注入，默认为true
     * 如果为true且找不到要注入的bean，则抛出异常
     */
    boolean required() default true;
} 