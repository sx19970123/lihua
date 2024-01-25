package com.lihua.system.service.impl;

import com.lihua.cache.RedisCache;
import com.lihua.config.LihuaConfig;
import com.lihua.model.security.*;
import com.lihua.enums.SysBaseEnum;
import com.lihua.system.mapper.SysRoleMapper;
import com.lihua.system.service.SysAuthenticationService;
import com.lihua.system.service.SysMenuService;
import com.lihua.system.service.SysUserStarViewService;
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
    private SysUserStarViewService sysUserStarViewService;

    @Resource
    private LihuaConfig lihuaConfig;

    @Transactional
    @Override
    public String login(SysUserVO sysUserVO) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(sysUserVO.getUsername(), sysUserVO.getPassword()));
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        // 处理登录用户信息，将用户基本数据存入 LoginUser 后存入 redis
        handleLoginInfo(loginUser);
        // 根据username 生成jwt 返回
        return JwtUtils.create(loginUser.getSysUserVO().getId());
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
        String id = loginUser.getSysUserVO().getId();
        // 隐藏密码
        loginUser.getSysUserVO().setPassword(null);

        // 菜单router信息
        List<RouterVO> routerVOList = sysMenuService.selectSysMenuByLoginUserId(id);
        // 角色信息
        List<SysRoleVO> sysRoles = sysRoleMapper.selectSysRoleByUserId(id);
        // 收藏/固定菜单
        List<SysUserStarViewVO> starViewVOList = sysUserStarViewService.selectByUserId(id);

        loginUser
            .setRouterList(routerVOList)
            .setSysRoleList(sysRoles)
            .setStarViewVOList(starViewVOList);

        // 将用户信息存放到redis
        redisCache.setCacheObject(SysBaseEnum.LOGIN_USER_REDIS_PREFIX.getValue() + loginUser.getSysUserVO().getId(), loginUser, lihuaConfig.getExpireTime(), TimeUnit.MINUTES);
    }
}
