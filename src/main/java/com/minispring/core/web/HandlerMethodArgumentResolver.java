package com.minispring.core.web;

import javax.servlet.http.HttpServletRequest;

/**
 * 处理器方法参数解析器接口
 */
public interface HandlerMethodArgumentResolver {
    /**
     * 判断是否支持参数解析
     * @param parameter 方法参数
     * @return 是否支持
     */
    boolean supportsParameter(MethodParameter parameter);

    /**
     * 解析参数
     * @param parameter 方法参数
     * @param request HTTP请求
     * @return 解析后的参数值
     * @throws Exception 解析异常
     */
    Object resolveArgument(MethodParameter parameter, HttpServletRequest request) throws Exception;
} 