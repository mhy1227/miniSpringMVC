package com.minispring.core.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * JSP视图实现
 */
public class JspView implements View {
    private final String url;

    public JspView(String url) {
        this.url = url;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 将模型数据添加到请求属性中
        if (model != null) {
            model.forEach(request::setAttribute);
        }

        // 转发到JSP页面
        request.getRequestDispatcher(url).forward(request, response);
    }

    public String getUrl() {
        return url;
    }
} 