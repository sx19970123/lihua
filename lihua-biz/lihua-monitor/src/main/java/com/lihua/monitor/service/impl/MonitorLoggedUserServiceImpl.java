package com.lihua.monitor.service.impl;

import com.lihua.web.utils.WebUtils;
import com.lihua.monitor.model.LoggedUser;
import com.lihua.cache.manager.RedisCacheManager;
import com.lihua.cache.enums.RedisKeyPrefixEnum;
import com.lihua.common.exception.ServiceException;
import com.lihua.security.manager.LoginUserManager;
import com.lihua.security.model.CurrentUser;
import com.lihua.security.model.LoginUserSession;
import com.lihua.monitor.service.MonitorLoggedUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
public class MonitorLoggedUserServiceImpl implements MonitorLoggedUserService {

    @Resource
    private RedisCacheManager redisCacheManager;

    @Override
    public List<LoggedUser> queryList(String username, String nickName, String clientType) {

        // 获取登录中用户所有key
        Set<String> keys = redisCacheManager.keys(RedisKeyPrefixEnum.LOGIN_USER_REDIS_PREFIX.getValue());

        // 批量读取会话（MGET 分批，替代逐 key GET 的 N+1：千级会话从千余次往返降为约 1/100；
        // 会话可能在扫描与读取之间过期，缺失 key 自然不在结果中）
        List<LoginUserSession> loginUserSessions = new ArrayList<>(redisCacheManager.getCacheObjects(keys, LoginUserSession.class).values());

        // 根据用户名过滤
        if (StringUtils.hasText(username)) {
            loginUserSessions = loginUserSessions.stream()
                    .filter(user -> StringUtils.hasText(user.getUsername()) && user.getUsername().contains(username))
                    .toList();
        }

        // 根据用户nickname过滤
        if (StringUtils.hasText(nickName)) {
            loginUserSessions = loginUserSessions.stream()
                    .filter(user -> StringUtils.hasText(user.getUser().getNickname()) && user.getUser().getNickname().contains(nickName))
                    .toList();
        }

        // 根据用户登录客户端过滤
        if (StringUtils.hasText(clientType)) {
            loginUserSessions = loginUserSessions.stream()
                    .filter(user -> StringUtils.hasText(user.getClientType()) && user.getClientType().contains(clientType))
                    .toList();
        }

        // 转为 LoggedUser 对象返回（按登录时间倒序：keys 哈希迭代序不稳定，不排序则每次刷新顺序漂移）
        return loginUserSessions.stream().map(user -> {
            String cacheKey = user.getCacheKey();
            CurrentUser currentUser = user.getUser();
            LoggedUser loggedUser = new LoggedUser();
            loggedUser.setUsername(currentUser.getUsername());
            loggedUser.setNickname(currentUser.getNickname());
            loggedUser.setIp(user.getIpAddress());
            loggedUser.setRegion(WebUtils.getRegion(user.getIpAddress()));
            loggedUser.setCacheKey(cacheKey);
            loggedUser.setLoginTime(LoginUserManager.getLoginTimeByCacheKey(cacheKey));
            loggedUser.setClientType(user.getClientType());
            return loggedUser;
        })
        .sorted(Comparator.comparing(LoggedUser::getLoginTime, Comparator.nullsLast(Comparator.reverseOrder())))
        .toList();
    }

    @Override
    public void forceLogout(List<String> cacheKeys) {
        // 白名单校验：合法会话 key 为登录会话前缀的四段结构（前缀:userId:时间戳:uuid），
        // 防止任意 Redis key 被当作会话广播+删除（getUserIdByCacheKey 仅校验段数，此处叠加前缀）
        cacheKeys.forEach(cacheKey -> {
            if (!cacheKey.startsWith(RedisKeyPrefixEnum.LOGIN_USER_REDIS_PREFIX.getValue())
                    || cacheKey.split(":").length != 4) {
                throw new ServiceException("无效的 cacheKey");
            }
        });
        cacheKeys.forEach(LoginUserManager::removeLoginUserSession);
    }
}
