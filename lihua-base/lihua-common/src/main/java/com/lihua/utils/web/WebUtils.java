package com.lihua.utils.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
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
    public static void renderJson(HttpServletResponse response, String json) {
        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

    /**
     * 获取当前请求的HttpServletRequest
     */
    public static HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }


    /**
     * 获取客户端类型
     * @return web ｜ app ｜ wechat_mp ｜ null
     */
    public static String getClientType() {
        HttpServletRequest request = getCurrentRequest();
        return request.getHeader("Client-Type");
    }

}
