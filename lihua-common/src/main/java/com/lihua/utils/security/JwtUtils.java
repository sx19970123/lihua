package com.lihua.utils.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import static com.lihua.enums.SysBaseEnum.JWT_TOKEN_SECRET;

/**
 * 简单 JWT 加密解密工具类
 */
public class JwtUtils {

    /**
     * 将一个字符串key 进行jwt 加密
     */
    public static String create(String key) {
        return JWT
                .create()
                .withAudience(key)
                .sign(Algorithm.HMAC256(JWT_TOKEN_SECRET.getValue()));
    }

    /**
     * decode jwt
     */
    public static String decode(String jwtToken) {
        return JWT
                .decode(jwtToken)
                .getAudience()
                .get(0);
    }

    /**
     * 验证 jwt 是否合法
     */
    public static void verify(String jwtToken) {
        JWT
        .require(Algorithm.HMAC256(JWT_TOKEN_SECRET.getValue()))
        .build()
        .verify(jwtToken);
    }
}
