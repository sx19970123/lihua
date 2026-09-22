package com.lihua.security.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

/**
 * 简单 JWT 加密解密工具类（密钥由调用方传入——来自 TokenProperties.tokenSecret，不入代码）
 * <p>
 * 设计口径：token 仅作 cacheKey 的自包含载体，服务侧只 decode 不验签——
 * 会话事实源在 Redis cacheKey，凭证不可猜测性由 128bit uuid 承担
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
}
