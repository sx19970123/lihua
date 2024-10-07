package com.lihua.utils.security;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.lihua.exception.ServiceException;
import com.lihua.model.cryption.RasModel;
import com.lihua.utils.crypt.RasUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.concurrent.TimeUnit;

@Slf4j
public class SecurityUtils {

    // 密码加密缓存，作用类似ConcurrentHashMap，超过指定时间会移除对应的key
    private static final Cache<String, RasModel> PASSWORD_KEY_CACHE = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.HOURS)  // 1小时未被访问则过期
            .build();

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

    /**
     * 获取密码加密 RAS公钥
     * @param requestKey 请求key
     * @return 公钥
     */
    public static String getPasswordPublicKey(String requestKey) {
        RasModel rasModel = RasUtils.generateKey();
        PASSWORD_KEY_CACHE.put(requestKey, rasModel);
        return rasModel.getPublicKey();
    }

    /**
     * 对密码进行解密
     * @param ciphertext 密文
     * @param requestKey 请求key
     * @return 密码
     */
    public static String decryptGetPassword(String ciphertext, String requestKey) {
        RasModel rasModel = PASSWORD_KEY_CACHE.getIfPresent(requestKey);

        // rasModel为空表示缓存的加密信息已过期
        if (rasModel == null) {
            throw new ServiceException();
        }

        // 获取 RasModel 立即清除释放内存
        PASSWORD_KEY_CACHE.invalidate(requestKey);

        // 尝试获取密码，期间发生异常视为登录失败
        try {
            return RasUtils.decrypt(ciphertext, rasModel);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException();
        }
    }
}
