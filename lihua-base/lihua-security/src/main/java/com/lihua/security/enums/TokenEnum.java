package com.lihua.security.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenEnum {
    /**
     * JWT 密钥
     */
    JWT_TOKEN_SECRET("c8f2f1fa89b78f4862d6e2a6a8b5b4a5c8e8b7a3a1c0e8b9f1a7e8a6f0b9c8a7c8e7a9b7a5c8f7e4a8b6f9e2b3c7d9f8e6b5a3f2a9d8c7b9a4e8c7d8b9f4e7a9d3b9f6"),

    /**
     * 从cookie 获取 token 的 key
     */
    COOKIE_TOKEN_KEY("lihua_token"),

    /**
     * 从请求头 获取 token 的 key
     */
    TOKEN_KEY("Authorization"),

    /**
     * token 前缀
     */
    TOKEN_PREFIX("Bearer ");

    private final String value;
}
