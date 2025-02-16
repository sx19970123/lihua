package com.lihua.service.system.authentication.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lihua.cache.RedisCache;
import com.lihua.entity.system.SysSetting;
import com.lihua.entity.system.SysUser;
import com.lihua.enums.SysBaseEnum;
import com.lihua.exception.ServiceException;
import com.lihua.mapper.system.SysRoleMapper;
import com.lihua.mapper.system.SysUserMapper;
import com.lihua.model.security.*;
import com.lihua.service.system.authentication.SysAuthenticationService;
import com.lihua.service.system.profile.SysProfileService;
import com.lihua.service.system.setting.SysSettingService;
import com.lihua.model.system.dto.SysSettingDTO;
import com.lihua.strategy.system.authentication.CacheLoginUserStrategy;
import com.lihua.strategy.system.authentication.CheckLoginSettingStrategy;
import com.lihua.strategy.system.authentication.SaveRegisterUserAssociatedStrategy;
import com.lihua.utils.date.DateUtils;
import com.lihua.utils.json.JsonUtils;
import com.lihua.utils.security.JwtUtils;
import com.lihua.utils.security.LoginUserManager;
import com.lihua.utils.security.SecurityUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
        return  (LoginUser) authenticate.getPrincipal();
    }


    @Override
    public List<String> checkLoginSetting(LoginUser loginUser) {
        // 需要进行登陆后设置的组件名集合
        List<String> loginSettingComponentNameList = new ArrayList<>();

        // 将密码设置到LoginUser对象中
        String password = sysProfileService.getPassword();
        loginUser.getUser().setPassword(password);

        // 循环检查是否需要进行登陆后配置
        checkLoginSettingStrategyList.forEach(strategy -> {
            String componentName = strategy.checkSetting(loginUser);
            if (StringUtils.hasText(componentName)) {
                loginSettingComponentNameList.add(componentName);
            }
        });

        return loginSettingComponentNameList;
    }


    @Override
    public String cacheLoginUserInfo(LoginUser loginUser) {
        // 当前用户是否为管理员
        boolean isAdmin = isAdmin(loginUser.getUser().getId());
        // 执行各个模块的缓存设置
        cacheLoginUserStrategyList.forEach(strategy -> strategy.cacheLoginUser(loginUser, isAdmin));
        // 设置redis缓存
        return LoginUserManager.setLoginUserCache(loginUser);
    }

    @Override
    public String cacheAndCreateToken(LoginUser loginUser) {
        String redisKey = cacheLoginUserInfo(loginUser);
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
        SysSetting setting = sysSettingService.getSysSettingByComponentName("SignInSetting");

        if (setting == null) {
            throw new ServiceException("注册配置不存在");
        }

        // 自助注册配置
        SysSettingDTO.SignInSetting signInSetting = JsonUtils.toObject(setting.getSettingJson(), SysSettingDTO.SignInSetting.class);

        if (!signInSetting.isEnable()) {
            throw new ServiceException("用户注册未开放");
        }

        LocalDateTime now = DateUtils.now();

        // 用户基本信息
        SysUser sysUser = new SysUser();
        sysUser.setUsername(username);
        sysUser.setPassword(SecurityUtils.encryptPassword(password));
        sysUser.setCreateTime(now);
        sysUser.setDelFlag("0");
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
        // 获取最大登录用户配置信息
        SysSetting sameAccountLoginSetting = sysSettingService.getSysSettingByComponentName("SameAccountLoginSetting");

        if (sameAccountLoginSetting == null) {
            return;
        }
        SysSettingDTO.SameAccountLoginSetting setting = JsonUtils.toObject(sameAccountLoginSetting.getSettingJson(), SysSettingDTO.SameAccountLoginSetting.class);
        // 是否启用
        if (!setting.isEnable()) {
            return;
        }
        // 获取设定最大登录用户数
        int limitSize = setting.getMaximum() > 0 ? setting.getMaximum() : 1;
        // 获取用户id
        String userId = LoginUserManager.getUserIdByCacheKey(JwtUtils.decode(token));

        if (!StringUtils.hasText(userId)) {
            throw new ServiceException("用户id不存在");
        }

        // 获取所有用户登录 key
        Set<String> keys = redisCache.keys(SysBaseEnum.LOGIN_USER_REDIS_PREFIX.getValue() + userId);

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

    /**
     * 判断当前登录用户是否为超级管理员
     */
    private boolean isAdmin(String userId) {
        List<String> roleCodes = sysRoleMapper.selectCodeByUserId(userId);
        return roleCodes.contains("ROLE_admin");
    }

}
