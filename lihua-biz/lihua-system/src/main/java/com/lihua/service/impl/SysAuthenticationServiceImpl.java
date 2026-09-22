package com.lihua.service.impl;

import com.lihua.common.enums.RegisterTypeEnum;
import com.lihua.common.enums.SysStatusEnum;
import com.lihua.common.exception.ServiceException;
import com.lihua.common.utils.date.DateUtils;
import com.lihua.entity.SysUser;
import com.lihua.mapper.SysRoleMapper;
import com.lihua.mapper.SysUserMapper;
import com.lihua.model.dto.SysLoginUserDTO;
import com.lihua.model.dto.SysSettingDTO;
import com.lihua.cache.manager.RedisCacheManager;
import com.lihua.cache.enums.RedisKeyPrefixEnum;
import com.lihua.security.manager.LoginUserContext;
import com.lihua.security.manager.LoginUserManager;
import com.lihua.security.config.LoginLockProperties;
import com.lihua.security.config.TokenProperties;
import com.lihua.security.model.LoginUserSession;
import com.lihua.security.utils.JwtUtils;
import com.lihua.security.utils.SecurityUtils;
import com.lihua.service.SysAuthenticationService;
import com.lihua.service.SysSettingService;
import com.lihua.service.SysUserService;
import com.lihua.strategy.cacheloginuser.CacheLoginUserStrategy;
import com.lihua.strategy.saveuserregister.SaveRegisterUserAssociatedStrategy;
import com.lihua.web.utils.WebUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class SysAuthenticationServiceImpl implements SysAuthenticationService {

    /**
     * 登录失败锁定：窗口内失败达阈值后锁定（账号与 ip 双维度独立计数与锁定——防针对单账号撞库与单源撞多账号）；
     * 系统级故障（InternalAuthenticationServiceException，如数据源不可用）不计失败，防服务故障期间误锁全部尝试用户。
     * 参数与开关见 login.lock 配置（LoginLockProperties）
     */

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysSettingService sysSettingService;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private RedisCacheManager redisCacheManager;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private LoginLockProperties loginLockProperties;

    @Resource
    private List<CacheLoginUserStrategy> cacheLoginUserStrategyList;

    @Resource
    private List<SaveRegisterUserAssociatedStrategy> saveRegisterUserAssociatedStrategyList;

    @Resource
    private TokenProperties tokenProperties;

    @Override
    public LoginUserSession login(SysLoginUserDTO loginUserDTO) {
        String username = loginUserDTO.getUsername();
        checkLoginLock(username);
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, loginUserDTO.getPassword()));
            // 成功即清账号维度失败计数（ip 维度留窗口自然过期，继续压制同源撞库）
            redissonClient.getAtomicLong(loginFailCountKey(username)).delete();
            return (LoginUserSession) authenticate.getPrincipal();
        } catch (InternalAuthenticationServiceException e) {
            throw e;
        } catch (AuthenticationException e) {
            recordLoginFail(username);
            throw e;
        }
    }

    /**
     * 锁定检查：账号或 ip 任一在锁即拒绝（锁未过期时 authenticate 不执行，计数不再增长）
     */
    private void checkLoginLock(String username) {
        if (!loginLockProperties.isEnabled()) {
            return;
        }
        String ip = WebUtils.getIpAddress();
        if (redissonClient.getBucket(loginLockKey(username), StringCodec.INSTANCE).isExists()
                || (ip != null && redissonClient.getBucket(loginLockKey(ip), StringCodec.INSTANCE).isExists())) {
            throw new ServiceException("尝试次数过多，请 " + loginLockProperties.getLockDuration().toMinutes() + " 分钟后再试");
        }
    }

    /**
     * 失败计数：账号与 ip 双维度各自在窗口内累计，任一达阈值写对应锁
     */
    private void recordLoginFail(String username) {
        if (!loginLockProperties.isEnabled()) {
            return;
        }
        if (incrLoginFail(username)) {
            redissonClient.getBucket(loginLockKey(username), StringCodec.INSTANCE).set("1", loginLockProperties.getLockDuration());
        }
        String ip = WebUtils.getIpAddress();
        if (ip != null && incrLoginFail(ip)) {
            redissonClient.getBucket(loginLockKey(ip), StringCodec.INSTANCE).set("1", loginLockProperties.getLockDuration());
        }
    }

    /**
     * 计数并返回是否达阈值；首次计数时设置窗口 TTL（窗口自首次失败起算，固定窗不滚动）
     */
    private boolean incrLoginFail(String subject) {
        RAtomicLong counter = redissonClient.getAtomicLong(loginFailCountKey(subject));
        long count = counter.incrementAndGet();
        if (count == 1) {
            counter.expire(loginLockProperties.getFailWindow());
        }
        return count >= loginLockProperties.getFailThreshold();
    }

    private String loginFailCountKey(String subject) {
        return RedisKeyPrefixEnum.LOGIN_FAIL_COUNT_REDIS_PREFIX.getValue() + subject;
    }

    private String loginLockKey(String subject) {
        return RedisKeyPrefixEnum.LOGIN_LOCK_REDIS_PREFIX.getValue() + subject;
    }

    @Override
    public String cacheLoginUserInfo(LoginUserSession loginUserSession) {
        // 当前用户是否为管理员
        boolean isAdmin = isAdmin(loginUserSession.getUser().getId());
        // permissionList 由 role/menu 两个策略合并写入（null 则 set、非 null 则 addAll），
        // 重复重载会在既有列表上继续追加造成条目累积，先清空保证每次全量重建
        loginUserSession.setPermissionList(null);
        // 执行各个模块的缓存设置
        cacheLoginUserStrategyList.forEach(strategy -> strategy.cacheLoginUser(loginUserSession, isAdmin));
        // 设置redis缓存
        return LoginUserManager.setLoginUserCache(loginUserSession);
    }

    @Override
    public String cacheAndCreateToken(LoginUserSession loginUserSession) {
        String redisKey = cacheLoginUserInfo(loginUserSession);
        return JwtUtils.create(redisKey, tokenProperties.getTokenSecret());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String register(String username, String password) {

        SysSettingDTO.SignUpSetting signUpSetting = sysSettingService.getSignUpSetting();

        if (signUpSetting == null || !signUpSetting.isEnable()) {
            throw new ServiceException("用户注册未开放");
        }

        // 校验用户名
        boolean checked = sysUserService.checkUserName(username);
        if (!checked) {
            throw new ServiceException("该用户名已存在");
        }

        LocalDateTime now = DateUtils.now();

        // 用户基本信息
        SysUser sysUser = new SysUser();
        sysUser.setUsername(username);
        sysUser.setPassword(SecurityUtils.encryptPassword(password));
        sysUser.setStatus(SysStatusEnum.NORMAL.getValue());
        sysUser.setRegisterType(RegisterTypeEnum.SELF_REGISTER.getValue());
        sysUser.setPasswordUpdateTime(now);

        // 保存用户基本信息
        sysUserMapper.insert(sysUser);

        // 通过用户注册配置类保存相关关联表数据
        saveRegisterUserAssociatedStrategyList.forEach(strategy -> strategy.saveRegisterUserAssociated(sysUser.getId(), signUpSetting));

        return sysUser.getId();
    }

    @Override
    public void checkSameAccount(String token) {
        // 获取最大登录用户配置信息，-1为未配置
        int limitSize = sysSettingService.getMaxConcurrentLogins();

        if (limitSize == -1) {
            return;
        }

        // 获取用户id
        String userId = LoginUserManager.getUserIdByCacheKey(JwtUtils.decode(token));

        if (!StringUtils.hasText(userId)) {
            throw new ServiceException("用户id不存在");
        }

        // 获取所有用户登录 key（尾冒号限定精确用户段，防止 1 命中 10/11… 跨用户误踢）
        Set<String> keys = redisCacheManager.keys(RedisKeyPrefixEnum.LOGIN_USER_REDIS_PREFIX.getValue() + userId + ":");

        int count = keys.size() - limitSize;
        if (count < 0) {
            return;
        }

        // 根据用户登录时间，先登录的被踢下线
        keys.stream()
            .sorted(Comparator.comparingLong(LoginUserManager::getLoginTimestampByCacheKey))
            .limit(count)
            .forEach(LoginUserManager::removeLoginUserSession);
    }

    @Override
    public String getOnceToken() {
        String uuid = UUID.randomUUID().toString();
        redisCacheManager.setCacheObject(RedisKeyPrefixEnum.ONCE_TOKEN_REDIS_PREFIX.getValue() + uuid, LoginUserContext.getUserId(), Duration.ofMinutes(1));
        return uuid;
    }

    /**
     * 是否为超级管理员
     */
    private boolean isAdmin(String userId) {
        List<String> roleCodes = sysRoleMapper.selectCodeByUserId(userId);
        return roleCodes.contains("ROLE_admin");
    }

}
