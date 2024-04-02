package com.lihua.system.service.impl;

import com.lihua.cache.RedisCache;
import com.lihua.config.LihuaConfig;
import com.lihua.model.security.*;
import com.lihua.system.mapper.SysRoleMapper;
import com.lihua.system.service.SysAuthenticationService;
import com.lihua.system.service.SysMenuService;
import com.lihua.system.service.SysViewTabService;
import com.lihua.utils.security.JwtUtils;
import com.lihua.utils.security.LoginUserMgmt;
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
    private SysViewTabService sysViewTabService;

    @Resource
    private LihuaConfig lihuaConfig;

    @Transactional
    @Override
    public String login(SysUserVO sysUserVO) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(sysUserVO.getUsername(), sysUserVO.getPassword()));
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        // 处理登录用户信息，将用户基本数据存入 LoginUser 后存入 redis
        cacheUserLoginDetails(loginUser);
        // 根据username 生成jwt 返回
        return JwtUtils.create(loginUser.getSysUserVO().getId());
    }

    /**
     * 根据业务需要，可将用户数据存入 LoginUser 业务中可直接获取使用
     * @param loginUser
     */
    @Override
    public void cacheUserLoginDetails(LoginUser loginUser) {
        String id = loginUser.getSysUserVO().getId();
        // 菜单router信息
        List<RouterVO> routerVOList = sysMenuService.selectSysMenuByLoginUserId(id);
        // 角色信息
        List<SysRoleVO> sysRoles = sysRoleMapper.selectSysRoleByUserId(id);
        // 收藏/固定菜单
        List<SysViewTabVO> viewTabVOS = sysViewTabService.selectByUserId(id);
        // viewTab赋值routerPathKey
        handleSetViewTabKey(viewTabVOS,routerVOList);

        loginUser
            .setRouterList(routerVOList)
            .setSysRoleList(sysRoles)
            .setStarViewVOList(viewTabVOS);

        // 设置redis缓存
        LoginUserMgmt.setLoginUserCache(loginUser);
    }

    private void handleSetViewTabKey(List<SysViewTabVO> viewTabVOS,List<RouterVO> routerVOList) {
        viewTabVOS.forEach(tab -> {
            routerVOList.forEach(item -> {
                if (item.getId().equals(tab.getMenuId())) {
                    tab.setRouterPathKey(item.getKey());
                }
                if (item.getChildren() != null && !item.getChildren().isEmpty()) {
                    handleSetViewTabKey(viewTabVOS,item.getChildren());
                }
            });
        });

    }
}
