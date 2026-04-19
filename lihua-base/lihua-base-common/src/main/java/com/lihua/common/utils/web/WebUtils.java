package com.lihua.common.utils.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

/**
 * web相关工具类
 */
public class WebUtils {

    /**
     * 将 json 数据进行响应
     */
    @SneakyThrows
    public static void renderJson(String json) {
        HttpServletResponse response = getCurrentResponse();
        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

    /**
     * 获取当前请求的 HttpServletRequest
     */
    public static HttpServletRequest getCurrentRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        return ((ServletRequestAttributes) Objects.requireNonNull(requestAttributes)).getRequest();
    }

    /**
     * 获取当前请求的 HttpServletResponse
     */
    public static HttpServletResponse getCurrentResponse() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        return ((ServletRequestAttributes) Objects.requireNonNull(requestAttributes)).getResponse();
    }

    /**
     * 获取客户端类型
     * @return web ｜ app ｜ wechat_mp ｜ null
     */
    public static String getClientType() {
        HttpServletRequest request = getCurrentRequest();
        return getClientType(request);
    }

    /**
     * 根据request获取客户端类型
     */
    public static String getClientType(HttpServletRequest request) {
        return request.getHeader("Client-Type");
    }

}
