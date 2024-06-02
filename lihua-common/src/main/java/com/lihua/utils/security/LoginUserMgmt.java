package com.lihua.utils.security;

import com.lihua.cache.RedisCache;
import com.lihua.config.LihuaConfig;
import com.lihua.enums.SysBaseEnum;
import com.lihua.exception.ServiceException;
import com.lihua.model.security.LoginUser;
import com.lihua.utils.date.DateUtils;
import com.lihua.utils.spring.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.TimeUnit;

@Slf4j
public class LoginUserMgmt {

    /**
     * 获取spring容器中的redis缓存
     * @return
     */
    private static RedisCache redisCache() {
        return SpringUtils.getBean(RedisCache.class);
    }

    /**
     * 获取spring容器中的配置信息
     * @return
     */
    private static LihuaConfig lihuaConfig() {
        return SpringUtils.getBean(LihuaConfig.class);
    }

    /**
     * 根据 token 获取用户信息
     * @param token
     * @return
     */
    public static LoginUser getLoginUser(String token) {
        try {
            JwtUtils.verify(token);
        } catch (Exception e) {
            throw new ServiceException("无效的token");
        }

        String decode = JwtUtils.decode(token);
        log.debug("\n当前用户token为：{}\n解密后主键id为：{}", token, decode);

        try {
            return redisCache().getCacheObject(SysBaseEnum.LOGIN_USER_REDIS_PREFIX.getValue() + decode);
        } catch (Exception e) {
            log.error("从redis获取LoginUser发生异常，请检查redis状态", e);
        }
        return null;
    }

    /**
     * 过期时间小于指定时间时进行刷新
     */
    public static void verifyLoginUserCache() {
        LoginUser loginUser = LoginUserContext.getLoginUser();
        if (DateUtils.differenceMinute(loginUser.getExpirationTime(),DateUtils.now()) < lihuaConfig().getRefreshThreshold()) {
            setLoginUserCache(loginUser);
        }
    }

    /**
     * 设置 redis 缓存
     * @param loginUser
     */
    public static void setLoginUserCache(LoginUser loginUser) {
        // 记录过期时间
        loginUser.setExpirationTime(DateUtils.now().plusMinutes(lihuaConfig().getExpireTime()));
        // 设置缓存
        redisCache().setCacheObject(SysBaseEnum.LOGIN_USER_REDIS_PREFIX.getValue() + loginUser.getUser().getId(),
                loginUser,
                lihuaConfig().getExpireTime(),
                TimeUnit.MINUTES);
    }

    /**
     * 删除用户缓存
     * @param token
     */
    public static void removeLoginUserCache(String token) {
        String decode = JwtUtils.decode(token);
        redisCache().delete(SysBaseEnum.LOGIN_USER_REDIS_PREFIX.getValue() + decode);
    }

}
