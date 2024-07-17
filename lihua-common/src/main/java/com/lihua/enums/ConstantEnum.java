package com.lihua.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConstantEnum {

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
