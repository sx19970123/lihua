package com.lihua.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Constant {
    REDIS_LOCK("REDIS_LOCK"),
    ERROR_MSG("ERROR_MSG"),
    TOKEN("Authorization");
    private String code;
}
