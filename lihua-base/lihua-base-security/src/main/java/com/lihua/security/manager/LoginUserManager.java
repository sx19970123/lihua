package com.lihua.security.manager;

import com.lihua.cache.enums.RedisTopicEnum;
import com.lihua.cache.manager.LocalCacheManager;
import com.lihua.cache.publisher.RedisPublisher;
import com.lihua.common.exception.ServiceException;
import com.lihua.common.utils.date.DateUtils;
import com.lihua.common.utils.spring.SpringUtils;
import com.lihua.common.utils.web.WebUtils;
import com.lihua.cache.manager.RedisCacheManager;
import com.lihua.cache.enums.RedisKeyPrefixEnum;
import com.lihua.security.config.TokenConfig;
import com.lihua.security.model.LoginUser;
import com.lihua.security.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Slf4j
public class LoginUserManager {

    private static final RedisCacheManager REDIS_CACHE_MANAGER = SpringUtils.getBean(RedisCacheManager.class);

    private static final LocalCacheManager LOCAL_CACHE_MANAGER = SpringUtils.getBean(LocalCacheManager.class);

    private static final RedisPublisher REDIS_PUBLISHER = SpringUtils.getBean(RedisPublisher.class);

    private static final TokenConfig  tokenConfig = SpringUtils.getBean(TokenConfig.class);

    /**
     * 根据 token 获取用户信息
     */
    public static LoginUser getLoginUser(String token) {

        String decode = JwtUtils.decode(token);
        log.debug("\ntoken：【{}】\ndecode：【{}】", token, decode);

        try {
            // 优先使用本地缓存
            LoginUser loginUser = LOCAL_CACHE_MANAGER.getWithFallback(decode, LoginUser.class, REDIS_CACHE_MANAGER::getCacheObject);
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
        if (DateUtils.differenceMinute(DateUtils.now(), loginUser.getExpirationTime()) < tokenConfig.getRefreshThreshold()) {
            REDIS_CACHE_MANAGER.setExpire(loginUser.getCacheKey(), Duration.ofMinutes(tokenConfig.getTokenExpireTime()));
        }
    }

    /**
     * 设置 manager 缓存
     * @param loginUser 登录用户信息
     * @return redis缓存key
     */
    public static String setLoginUserCache(LoginUser loginUser) {
        // 记录过期时间
        loginUser.setExpirationTime(DateUtils.now().plusMinutes(tokenConfig.getTokenExpireTime()));
        // 隐藏用户密码
        loginUser.getUser().setPassword(null);
        // 登录客户端类型
        loginUser.setClientType(WebUtils.getClientType());

        // 当 loginUser 的 cacheKey 不存在，即为新登录用户，重新生成cacheKey，其余情况均为刷新缓存
        String cacheKey = loginUser.getCacheKey();
        if (!StringUtils.hasText(cacheKey)) {
            cacheKey = getLoginUserKey(loginUser.getUser().getId());
        }
        loginUser.setCacheKey(cacheKey);
        // 发送缓存失效广播
        REDIS_PUBLISHER.send(RedisTopicEnum.INVALIDATE_LOCAL_CACHE.getValue(), cacheKey);
        // 设置缓存
        REDIS_CACHE_MANAGER.setCacheObject(cacheKey,
                loginUser,
                Duration.ofMinutes(tokenConfig.getTokenExpireTime()));

        // 缓存key
        return cacheKey;
    }

    /**
     * 删除用户缓存
     */
    public static void removeLoginUserCache(String token) {
        String decode = JwtUtils.decode(token);
        // 发送缓存失效广播
        REDIS_PUBLISHER.send(RedisTopicEnum.INVALIDATE_LOCAL_CACHE.getValue(), decode);
        REDIS_CACHE_MANAGER.delete(decode);
    }

    /**
     * 获取 manager 存储的用户key
     * 用户key由四部分组成 1.固定前缀 2.用户id 3.当前时间戳 4.uuid随机数，中间由:连接
     */
    private static String getLoginUserKey(String userId) {
        return RedisKeyPrefixEnum.LOGIN_USER_REDIS_PREFIX.getValue()
                + userId + ":"
                + DateUtils.nowTimeStamp() + ":"
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
