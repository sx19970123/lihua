package com.lihua.utils.web;

import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

/**
 * 通过 HttpServletResponse 返回响应工具类
 */
public class WebUtils {

    /**
     * 将 json 数据进行响应
     * @param response
     * @param json
     */
    @SneakyThrows
    public static void renderJson(HttpServletResponse response, String json) {
        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
}
