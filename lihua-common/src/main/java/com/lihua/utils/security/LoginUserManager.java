package com.lihua.utils.security;

import com.lihua.cache.RedisCache;
import com.lihua.config.LihuaConfig;
import com.lihua.enums.SysBaseEnum;
import com.lihua.exception.ServiceException;
import com.lihua.model.security.LoginUser;
import com.lihua.utils.date.DateUtils;
import com.lihua.utils.spring.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
public class LoginUserManager {

    private static final RedisCache redisCache;

    static {
        redisCache = SpringUtils.getBean(RedisCache.class);
    }


    /**
     * 获取spring容器中的配置信息
     */
    private static LihuaConfig lihuaConfig() {
        return SpringUtils.getBean(LihuaConfig.class);
    }

    /**
     * 根据 token 获取用户信息
     */
    public static LoginUser getLoginUser(String token) {

        String decode = JwtUtils.decode(token);
        log.debug("\ntoken：【{}】\ndecode：【{}】", token, decode);

        try {
            LoginUser loginUser = redisCache.getCacheObject(decode, LoginUser.class);
            if (loginUser == null) {
                return null;
            }
            loginUser.setCacheKey(decode);
            return loginUser;
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
            redisCache.setExpire(loginUser.getCacheKey(), lihuaConfig().getTokenExpireTime(), TimeUnit.MINUTES);
        }
    }

    /**
     * 设置 redis 缓存
     * @param loginUser 登录用户信息
     * @return redis缓存key
     */
    public static String setLoginUserCache(LoginUser loginUser) {
        // 记录过期时间
        loginUser.setExpirationTime(DateUtils.now().plusMinutes(lihuaConfig().getTokenExpireTime()));

        // 当 loginUser 的 cacheKey 不存在，即为新登录用户，重新生成cacheKey，其余情况均为刷新缓存
        String cacheKey = loginUser.getCacheKey();
        if (!StringUtils.hasText(cacheKey)) {
            cacheKey = getLoginUserKey(loginUser.getUser().getId());
        }
        loginUser.setCacheKey(cacheKey);
        // 设置缓存
        redisCache.setCacheObject(cacheKey,
                loginUser,
                lihuaConfig().getTokenExpireTime(),
                TimeUnit.MINUTES);

        // 缓存key
        return cacheKey;
    }

    /**
     * 删除用户缓存
     */
    public static void removeLoginUserCache(String token) {
        String decode = JwtUtils.decode(token);
        redisCache.delete(decode);
    }

    /**
     * 获取 redis 存储的用户key
     * 用户key由四部分组成 1.固定前缀 2.用户id 3.当前时间戳 4.uuid随机数，中间由:连接
     */
    private static String getLoginUserKey(String userId) {
        return SysBaseEnum.LOGIN_USER_REDIS_PREFIX.getValue()
                + userId + ":"
                + System.currentTimeMillis() + ":"
                + UUID.randomUUID().toString().replace("-", "");

    }

    /**
     * 通过缓存cacheKey获取用户id
     */
    public static String getUserIdByCacheKey(String cacheKey) {
        if (!StringUtils.hasText(cacheKey)) {
            throw new ServiceException("空的 cacheKey");
        }
        String[] keySplit = cacheKey.split(":");

        if (keySplit.length != 4) {
            throw new ServiceException("无效的 cacheKey");
        }

        return keySplit[1];
    }

    /**
     * 通过缓存key获取用户登录时间戳
     */
    public static long getLoginTimestampByCacheKey(String cacheKey) {
        if (!StringUtils.hasText(cacheKey)) {
            throw new ServiceException("空的 cacheKey");
        }
        String[] keySplit = cacheKey.split(":");

        if (keySplit.length != 4) {
            throw new ServiceException("无效的 cacheKey");
        }

        return Long.parseLong(keySplit[2]);
    }

    /**
     * 通过缓存key获取用户登录时间
     */
    public static LocalDateTime getLoginTimeByCacheKey(String cacheKey) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(getLoginTimestampByCacheKey(cacheKey)), ZoneId.systemDefault());
    }

}
