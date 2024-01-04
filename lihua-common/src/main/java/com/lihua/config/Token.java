package com.lihua.config;

import lombok.Data;

@Data
public class Token {
    // redis 过期时间
    private Long expireTime;
}
