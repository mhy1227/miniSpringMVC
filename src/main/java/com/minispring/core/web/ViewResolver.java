package com.minispring.core.web;

/**
 * 视图解析器接口
 * 负责将视图名称解析为具体的视图对象
 */
public interface ViewResolver {
    /**
     * 将视图名称解析为视图对象
     * @param viewName 视图名称
     * @return 解析得到的视图对象
     * @throws Exception 解析过程中的异常
     */
    View resolveViewName(String viewName) throws Exception;
} 