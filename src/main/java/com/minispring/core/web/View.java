package com.minispring.core.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 视图接口
 * 定义视图渲染的标准行为
 */
public interface View {
    /**
     * 渲染视图
     * @param model 模型数据
     * @param request HTTP请求
     * @param response HTTP响应
     * @throws Exception 渲染过程中的异常
     */
    void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception;
} 