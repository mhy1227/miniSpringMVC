package com.minispring.core.web;

import java.util.Objects;

/**
 * 内部资源视图解析器
 * 支持JSP视图的解析，可配置视图前缀和后缀
 */
public class InternalResourceViewResolver implements ViewResolver {
    private String prefix = "";
    private String suffix = "";

    @Override
    public View resolveViewName(String viewName) throws Exception {
        Objects.requireNonNull(viewName, "View name must not be null");
        return new JspView(prefix + viewName + suffix);
    }

    public void setPrefix(String prefix) {
        this.prefix = (prefix != null ? prefix : "");
    }

    public void setSuffix(String suffix) {
        this.suffix = (suffix != null ? suffix : "");
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }
} 