package com.lihua.exception.security;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class InvalidTokenException extends RuntimeException {

    private String msg;

    public InvalidTokenException(String msg) {
        super(msg);
        this.msg = msg;
    }
}
