package com.minispring.core.annotation;

import java.lang.annotation.*;

/**
 * 标记一个类为组件
 * Spring容器会自动扫描被此注解标记的类，并将其实例化为Bean
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {
    /**
     * 组件的名称，默认为空字符串
     */
    String value() default "";
} 