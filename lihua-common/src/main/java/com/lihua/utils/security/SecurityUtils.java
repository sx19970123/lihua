package com.lihua.utils.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SecurityUtils {

    /**
     * 判断密码是否相同
     * @param password
     * @param encodedPassword
     * @return
     */
    public static Boolean matchesPassword(String password,String encodedPassword) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.matches(password,encodedPassword);
    }

    /**
     * 密码加密
     * @param password
     * @return
     */
    public static String encryptPassword(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(password);
    }
}
