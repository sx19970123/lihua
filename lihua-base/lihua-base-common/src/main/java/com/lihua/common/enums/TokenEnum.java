package com.lihua.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenEnum {

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
