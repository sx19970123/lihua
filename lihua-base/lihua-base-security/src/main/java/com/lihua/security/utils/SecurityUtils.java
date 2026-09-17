package com.lihua.security.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
public class SecurityUtils {

    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    /**
     * 判断密码是否相同
     */
    public static boolean matchesPassword(String password,String encodedPassword) {
        return PASSWORD_ENCODER.matches(password,encodedPassword);
    }

    /**
     * 密码加密
     */
    public static String encryptPassword(String password) {
        return PASSWORD_ENCODER.encode(password);
    }
}
