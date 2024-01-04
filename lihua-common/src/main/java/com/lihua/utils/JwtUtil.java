package com.lihua.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import static com.lihua.enums.SysBaseEnum.JWT_TOKEN_SECRET;

/**
 * 简单 JWT 加密解密工具类
 */
public class JwtUtil {

    /**
     * 将一个字符串key 进行jwt 加密
     * @param key
     * @return
     */
    public static String create(String key) {
        return JWT
                .create()
                .withAudience(key)
                .sign(Algorithm.HMAC256(JWT_TOKEN_SECRET.getValue()));
    }

    /**
     * decode jwt
     * @param jwtToken
     * @return
     */
    public static String decode(String jwtToken) {
        return JWT
                .decode(jwtToken)
                .getAudience()
                .get(0);
    }

    /**
     * 验证 jwt 是否合法
     * @param jwtToken
     */
    public static void verify(String jwtToken) {
        JWT
        .require(Algorithm.HMAC256(JWT_TOKEN_SECRET.getValue()))
        .build()
        .verify(jwtToken);
    }
}
