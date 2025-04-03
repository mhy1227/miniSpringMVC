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
        String parameterName = requestParam.value();
        String parameterValue = request.getParameter(parameterName);
        
        if (parameterValue == null && requestParam.required()) {
            throw new IllegalArgumentException(
                String.format("Required parameter '%s' is not present", parameterName));
        }
        
        Class<?> parameterType = parameter.getParameterType();
        if (parameterType == String.class) {
            return parameterValue;
        } else if (parameterType == int.class || parameterType == Integer.class) {
            return Integer.parseInt(parameterValue);
        } else if (parameterType == long.class || parameterType == Long.class) {
            return Long.parseLong(parameterValue);
        } else if (parameterType == double.class || parameterType == Double.class) {
            return Double.parseDouble(parameterValue);
        } else if (parameterType == boolean.class || parameterType == Boolean.class) {
            return Boolean.parseBoolean(parameterValue);
        }
        
        throw new IllegalArgumentException(
            String.format("Unsupported parameter type: %s", parameterType.getName()));
    }
} 