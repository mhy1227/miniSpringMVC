package com.minispring.core.web;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 处理器映射接口
 * 负责根据请求找到对应的处理方法
 */
public interface HandlerMapping {
    /**
     * 根据请求获取处理器
     * @param request HTTP请求
     * @return 处理器执行链
     */
    HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception;
} 