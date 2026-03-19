package com.lihua.security.utils;

import com.lihua.security.enums.TokenEnum;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

public class TokenUtils {

    /**
     * 从请求中获取 token
     */
    public static String getToken(HttpServletRequest request) {
        // 获取 token
        String token = null;
        if (request != null) {
            token = request.getHeader(TokenEnum.TOKEN_KEY.getValue());
        }
        if (StringUtils.hasText(token)) {
            return token.replace(TokenEnum.TOKEN_PREFIX.getValue(), "").trim();
        }

        return null;
    }
}
