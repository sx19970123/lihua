package com.lihua.utils.web;

import com.lihua.enums.ConstantEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Nullable;
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
     * 从请求中获取 token
     */
    public static String getToken(@Nullable HttpServletRequest request) {
        // 获取 token
        String token = null;
        if (request != null) {
            token = request.getHeader(ConstantEnum.TOKEN_KEY.getValue());
        }
        if (StringUtils.hasText(token)) {
            return token.replace(ConstantEnum.TOKEN_PREFIX.getValue(), "").trim();
        }

        return null;
    }

    /**
     * 获取当前请求的 ip地址
     * @return ip地址
     */
    public static String getIpAddress() {
        HttpServletRequest request = getCurrentRequest();
        String ip = request.getHeader("X-Forwarded-For");
        if (!StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (!StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
