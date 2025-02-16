package com.lihua.service.monitor.logged;

import com.lihua.cache.RedisCache;
import com.lihua.enums.SysBaseEnum;
import com.lihua.model.security.CurrentUser;
import com.lihua.model.security.LoginUser;
import com.lihua.model.monitor.LoggedUser;
import com.lihua.utils.security.LoginUserManager;
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
    public List<LoggedUser> queryList(String username, String nickName) {

        // 获取登录中用户所有key
        Set<String> keys = redisCache.keys(SysBaseEnum.LOGIN_USER_REDIS_PREFIX.getValue());

        // 取出所有登录用户信息
        List<LoginUser> loginUsers = new ArrayList<>();
        for (String key : keys) {
            loginUsers.add(redisCache.getCacheObject(key, LoginUser.class));
        }

        // 根据用户名过滤
        if (StringUtils.hasText(username)) {
            loginUsers = loginUsers.stream().filter(user -> user.getUsername().contains(username)).toList();
        }

        // 根据用户nickname过滤
        if (StringUtils.hasText(nickName)) {
            loginUsers = loginUsers.stream().filter(user -> user.getUser().getNickname().contains(nickName)).toList();
        }

        // 转为 LoggedUser 对象返回
        return loginUsers.stream().map(user -> {
            String cacheKey = user.getCacheKey();
            CurrentUser currentUser = user.getUser();
            LoggedUser loggedUser = new LoggedUser();
            loggedUser.setUsername(currentUser.getUsername());
            loggedUser.setNickname(currentUser.getNickname());
            loggedUser.setIp(user.getIpAddress());
            loggedUser.setCacheKey(cacheKey);
            loggedUser.setLoginTime(LoginUserManager.getLoginTimeByCacheKey(cacheKey));
            return loggedUser;
        }).toList();
    }

    @Override
    public void forceLogout(List<String> cacheKeys) {
        cacheKeys.forEach(cacheKey -> redisCache.delete(cacheKey));
    }
}
