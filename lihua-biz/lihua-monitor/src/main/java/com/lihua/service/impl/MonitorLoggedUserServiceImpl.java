package com.lihua.service.impl;

import com.lihua.ip.utils.IpUtils;
import com.lihua.model.LoggedUser;
import com.lihua.redis.cache.RedisCache;
import com.lihua.redis.enums.RedisKeyPrefixEnum;
import com.lihua.security.manager.LoginUserManager;
import com.lihua.security.model.CurrentUser;
import com.lihua.security.model.LoginUser;
import com.lihua.service.MonitorLoggedUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class MonitorLoggedUserServiceImpl implements MonitorLoggedUserService {

    @Resource
    private RedisCache redisCache;

    @Override
    public List<LoggedUser> queryList(String username, String nickName, String clientType) {

        // 获取登录中用户所有key
        Set<String> keys = redisCache.keys(RedisKeyPrefixEnum.LOGIN_USER_REDIS_PREFIX.getValue());

        // 取出所有登录用户信息
        List<LoginUser> loginUsers = new ArrayList<>();
        for (String key : keys) {
            loginUsers.add(redisCache.getCacheObject(key, LoginUser.class));
        }

        // 根据用户名过滤
        if (StringUtils.hasText(username)) {
            loginUsers = loginUsers.stream()
                    .filter(user -> StringUtils.hasText(user.getUsername()) && user.getUsername().contains(username))
                    .toList();
        }

        // 根据用户nickname过滤
        if (StringUtils.hasText(nickName)) {
            loginUsers = loginUsers.stream()
                    .filter(user -> StringUtils.hasText(user.getUser().getNickname()) && user.getUser().getNickname().contains(nickName))
                    .toList();
        }

        // 根据用户登录客户端过滤
        if (StringUtils.hasText(clientType)) {
            loginUsers = loginUsers.stream()
                    .filter(user -> StringUtils.hasText(user.getClientType()) && user.getClientType().contains(clientType))
                    .toList();
        }

        // 转为 LoggedUser 对象返回
        return loginUsers.stream().map(user -> {
            String cacheKey = user.getCacheKey();
            CurrentUser currentUser = user.getUser();
            LoggedUser loggedUser = new LoggedUser();
            loggedUser.setUsername(currentUser.getUsername());
            loggedUser.setNickname(currentUser.getNickname());
            loggedUser.setIp(user.getIpAddress());
            loggedUser.setRegion(IpUtils.getRegion(user.getIpAddress()));
            loggedUser.setCacheKey(cacheKey);
            loggedUser.setLoginTime(LoginUserManager.getLoginTimeByCacheKey(cacheKey));
            loggedUser.setClientType(user.getClientType());
            return loggedUser;
        }).toList();
    }

    @Override
    public void forceLogout(List<String> cacheKeys) {
        cacheKeys.forEach(cacheKey -> redisCache.delete(cacheKey));
    }
}
