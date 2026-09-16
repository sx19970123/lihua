package com.lihua.security.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

/**
 * 简单 JWT 加密解密工具类（密钥由调用方传入——来自 TokenProperties.tokenSecret，不入代码）
 */
public class JwtUtils {

    /**
     * 将一个字符串key 进行jwt 加密
     */
    public static String create(String key, String secret) {
        return JWT
                .create()
                .withAudience(key)
                .sign(Algorithm.HMAC256(secret));
    }

    /**
     * decode jwt
     */
    public static String decode(String jwtToken) {
        return JWT.decode(jwtToken).getAudience().get(0);
    }

    /**
     * 验证 jwt 是否合法
     */
    public static void verify(String jwtToken, String secret) {
        JWT
        .require(Algorithm.HMAC256(secret))
        .build()
        .verify(jwtToken);
    }
}
