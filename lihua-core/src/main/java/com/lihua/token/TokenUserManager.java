package com.lihua.token;

import com.lihua.cache.RedisCache;
import com.lihua.enums.SysBaseEnum;
import com.lihua.exception.security.InvalidTokenException;
import com.lihua.model.security.LoginUser;
import com.lihua.utils.security.JwtUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TokenUserManager {
    @Resource
    private RedisCache redisCache;

    /**
     * 通过 token 获取用户信息
     * @param token
     * @return
     */
    public LoginUser getUserInfoByToken(String token) {
        try {
            JwtUtils.verify(token);
        } catch (Exception e) {
            throw new InvalidTokenException("无效的token");
        }

        String decode = JwtUtils.decode(token);
        log.info("\n当前用户token为：{}\n解密后主键id为：{}", token, decode);

        try {
            return redisCache.getCacheObject(SysBaseEnum.LOGIN_USER_REDIS_PREFIX.getValue() + decode);
        } catch (Exception e) {
            log.error("从redis获取LoginUser发生异常，请检查redis状态");
            e.printStackTrace();
        }
        return null;
    }
}
