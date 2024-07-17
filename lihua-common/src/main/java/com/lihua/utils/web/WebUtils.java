package com.lihua.utils.web;

import com.lihua.enums.ConstantEnum;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

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

    /**
     * 从请求中获取 token
     * @param request
     * @return
     */
    public static String getToken(HttpServletRequest request) {
        // 获取 token
        String token = request.getHeader(ConstantEnum.TOKEN_KEY.getValue());
        if (StringUtils.hasText(token)) {
            return token.replace(ConstantEnum.TOKEN_PREFIX.getValue(), "");
        }

        // 从 cookie 获取 token
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (ConstantEnum.COOKIE_TOKEN_KEY.getValue().equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}
