package com.lihua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lihua.common.exception.ServiceException;
import com.lihua.common.utils.date.DateUtils;
import com.lihua.entity.SysUser;
import com.lihua.mapper.SysRoleMapper;
import com.lihua.mapper.SysUserMapper;
import com.lihua.model.dto.SysSettingDTO;
import com.lihua.redis.cache.RedisCache;
import com.lihua.redis.enums.RedisKeyPrefixEnum;
import com.lihua.security.manager.LoginUserContext;
import com.lihua.security.manager.LoginUserManager;
import com.lihua.security.model.CurrentUser;
import com.lihua.security.model.LoginUser;
import com.lihua.security.utils.JwtUtils;
import com.lihua.security.utils.SecurityUtils;
import com.lihua.service.SysAuthenticationService;
import com.lihua.service.SysProfileService;
import com.lihua.service.SysSettingService;
import com.lihua.strategy.cacheloginuser.CacheLoginUserStrategy;
import com.lihua.strategy.cacheloginuser.CacheUserStrategyImpl;
import com.lihua.strategy.checkloginsetting.CheckLoginSettingStrategy;
import com.lihua.strategy.saveuserregister.SaveRegisterUserAssociatedStrategy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class SysAuthenticationServiceImpl implements SysAuthenticationService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysSettingService sysSettingService;

    @Resource
    private SysProfileService sysProfileService;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private RedisCache redisCache;

    @Resource
    private List<CacheLoginUserStrategy> cacheLoginUserStrategyList;

    @Resource
    private List<SaveRegisterUserAssociatedStrategy> saveRegisterUserAssociatedStrategieList;

    @Resource
    private List<CheckLoginSettingStrategy> checkLoginSettingStrategyList;


    @Override
    public LoginUser login(CurrentUser currentUser) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(currentUser.getUsername(), currentUser.getPassword()));
        return (LoginUser) authenticate.getPrincipal();
    }


    @Override
    public List<String> checkLoginSetting(LoginUser loginUser) {
        // 需要进行登录后设置的组件名集合
        List<String> loginSettingComponentNameList = new ArrayList<>();

        // 将密码设置到LoginUser对象中
        String password = sysProfileService.getPassword();
        loginUser.getUser().setPassword(password);

        // 循环检查是否需要进行登录后配置
        checkLoginSettingStrategyList.forEach(strategy -> {
            String componentName = strategy.checkSetting(loginUser);
            if (StringUtils.hasText(componentName)) {
                loginSettingComponentNameList.add(componentName);
            }
        });

        return loginSettingComponentNameList;
    }


    @Override
    public String cacheLoginUserInfo(LoginUser loginUser, boolean isReload) {
        // 当前用户是否为管理员
        boolean isAdmin = isAdmin(loginUser.getUser().getId());
        // 执行各个模块的缓存设置
        cacheLoginUserStrategyList
                .stream()
                // 根据 isReload 标识判断是否执行 CacheUserStrategyImpl，否则登录时会查询两次
                .filter(strategy -> isReload || !(strategy instanceof CacheUserStrategyImpl))
                .forEach(strategy -> strategy.cacheLoginUser(loginUser, isAdmin));
        // 设置redis缓存
        return LoginUserManager.setLoginUserCache(loginUser);
    }

    @Override
    public String cacheAndCreateToken(LoginUser loginUser) {
        String redisKey = cacheLoginUserInfo(loginUser, false);
        return JwtUtils.create(redisKey);
    }

    @Override
    public boolean checkUserName(String username) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysUser::getUsername, username);
        return !sysUserMapper.exists(queryWrapper);
    }

    @Override
    @Transactional
    public String register(String username, String password) {

        SysSettingDTO.SignInSetting signInSetting = sysSettingService.getSignInSetting();

        if (signInSetting == null || !signInSetting.isEnable()) {
            throw new ServiceException("用户注册未开放");
        }

        // 校验用户名
        boolean checked = checkUserName(username);
        if (!checked) {
            throw new ServiceException("该用户名已存在");
        }

        LocalDateTime now = DateUtils.now();

        // 用户基本信息
        SysUser sysUser = new SysUser();
        sysUser.setUsername(username);
        sysUser.setPassword(SecurityUtils.encryptPassword(password));
        sysUser.setStatus("0");
        sysUser.setRegisterType("1");
        sysUser.setPasswordUpdateTime(now);

        // 保存用户基本信息
        sysUserMapper.insert(sysUser);

        // 通过用户注册配置类保存相关关联表数据
        saveRegisterUserAssociatedStrategieList.forEach(strategy -> strategy.saveRegisterUserAssociated(sysUser.getId(), signInSetting));

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

        // 获取所有用户登录 key
        Set<String> keys = redisCache.keys(RedisKeyPrefixEnum.LOGIN_USER_REDIS_PREFIX.getValue() + userId);

        int count = keys.size() - limitSize;
        if (count < 0) {
            return;
        }

        // 根据用户登录时间，先登录的被踢下线
        keys.stream()
            .sorted(Comparator.comparingLong(LoginUserManager::getLoginTimestampByCacheKey))
            .limit(count)
            .forEach(key -> redisCache.delete(key));
    }

    @Override
    public String getOnceToken() {
        String uuid = UUID.randomUUID().toString();
        redisCache.setCacheObject(RedisKeyPrefixEnum.ONCE_TOKEN_REDIS_PREFIX.getValue() + uuid, LoginUserContext.getUserId(), Duration.ofMinutes(1));
        return uuid;
    }

    /**
     * 判断当前登录用户是否为超级管理员
     */
    private boolean isAdmin(String userId) {
        List<String> roleCodes = sysRoleMapper.selectCodeByUserId(userId);
        return roleCodes.contains("ROLE_admin");
    }

}
