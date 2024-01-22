package com.lihua.system.service.impl;

import com.lihua.cache.RedisCache;
import com.lihua.config.LihuaConfig;
import com.lihua.entity.SysRole;
import com.lihua.enums.SysBaseEnum;
import com.lihua.model.LoginUser;
import com.lihua.entity.SysUser;
import com.lihua.model.RouterVO;
import com.lihua.system.mapper.SysRoleMapper;
import com.lihua.system.service.SysAuthenticationService;
import com.lihua.system.service.SysMenuService;
import com.lihua.utils.security.JwtUtils;
import com.lihua.utils.security.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class SysAuthenticationServiceImpl implements SysAuthenticationService {

    @Resource
    private RedisCache redisCache;
    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private LihuaConfig lihuaConfig;

    @Transactional
    @Override
    public String login(SysUser sysUser) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(sysUser.getUsername(), sysUser.getPassword()));
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        // 处理登录用户信息，将用户基本数据存入 LoginUser 后存入 redis
        handleLoginInfo(loginUser);
        // 根据username 生成jwt 返回
        return JwtUtils.create(loginUser.getSysUser().getId());
    }

    @Override
    public void logout() {
        redisCache.delete(SysBaseEnum.LOGIN_USER_REDIS_PREFIX.getValue() + SecurityUtils.getUserId());
    }


    /**
     * 根据业务需要，可将用户数据存入 LoginUser 业务中可直接获取使用
     * @param loginUser
     */
    private void handleLoginInfo(LoginUser loginUser) {
        String id = loginUser.getSysUser().getId();
        // 隐藏密码
        loginUser.getSysUser().setPassword(null);
        List<RouterVO> routerVOS = sysMenuService.initMetaRouterInfo(id);
        // 角色信息
        List<SysRole> sysRoles = sysRoleMapper.selectSysRoleByUserId(id);

        loginUser
            .setSysRoleList(sysRoles)
            .setRouterList(routerVOS);

        // 将用户信息存放到redis
        redisCache.setCacheObject(SysBaseEnum.LOGIN_USER_REDIS_PREFIX.getValue() + loginUser.getSysUser().getId(), loginUser, lihuaConfig.getExpireTime(), TimeUnit.MINUTES);
    }
}
