package com.lihua.exception.security;

import lombok.Data;

@Data
public class LoginExpiredException extends RuntimeException {
    private String msg;

    public LoginExpiredException(String msg) {
        super(msg);
        this.msg = msg;
    }
}
