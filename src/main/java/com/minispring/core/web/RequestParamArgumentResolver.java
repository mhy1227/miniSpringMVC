package com.minispring.core.web;

import com.minispring.core.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;

/**
 * 请求参数解析器
 * 处理带有@RequestParam注解的方法参数
 */
public class RequestParamArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request) throws Exception {
        RequestParam requestParam = parameter.getParameterAnnotation(RequestParam.class);
        String paramName = getParamName(parameter, requestParam);
        String paramValue = request.getParameter(paramName);
        
        // 处理参数不存在的情况
        if (paramValue == null) {
            if (requestParam.required()) {
                throw new IllegalArgumentException(
                    "Required parameter '" + paramName + "' is not present");
            }
            paramValue = requestParam.defaultValue();
        }
        
        // 转换参数类型
        return convertValue(paramValue, parameter.getParameterType());
    }

    private String getParamName(MethodParameter parameter, RequestParam requestParam) {
        String value = requestParam.value();
        return value.isEmpty() ? parameter.getParameterName() : value;
    }

    private Object convertValue(String value, Class<?> targetType) {
        if (String.class.equals(targetType)) {
            return value;
        }
        if (Integer.class.equals(targetType) || int.class.equals(targetType)) {
            return Integer.parseInt(value);
        }
        if (Long.class.equals(targetType) || long.class.equals(targetType)) {
            return Long.parseLong(value);
        }
        if (Double.class.equals(targetType) || double.class.equals(targetType)) {
            return Double.parseDouble(value);
        }
        if (Boolean.class.equals(targetType) || boolean.class.equals(targetType)) {
            return Boolean.parseBoolean(value);
        }
        throw new IllegalArgumentException(
            "Unsupported parameter type: " + targetType.getName());
    }
} 