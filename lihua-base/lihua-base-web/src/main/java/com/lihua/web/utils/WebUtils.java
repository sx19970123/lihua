package com.lihua.web.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.util.Objects;

/**
 * web相关工具类
 */
@Slf4j
public class WebUtils {

    /**
     * 将 json 数据进行响应
     */
    public static void renderJson(String json) {
        renderJson(200, json);
    }

    /**
     * 将 json 数据进行响应
     */
    @SneakyThrows
    public static void renderJson(int code, String json) { {
        HttpServletResponse response = getCurrentResponse();
        if (response == null) {
            log.error("响应数据写入失败，获取到的 HttpServletResponse 为空");
            return;
        }
        response.setStatus(code);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }}

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
        if (request == null) {
            return null;
        }
        return request.getHeader("Client-Type");
    }

}
