package com.lihua.system.service.impl;

import com.lihua.cache.RedisCache;
import com.lihua.config.LihuaConfig;
import com.lihua.model.security.*;
import com.lihua.system.mapper.SysDeptMapper;
import com.lihua.system.mapper.SysMenuMapper;
import com.lihua.system.mapper.SysPostMapper;
import com.lihua.system.mapper.SysRoleMapper;
import com.lihua.system.service.SysAuthenticationService;
import com.lihua.system.service.SysMenuService;
import com.lihua.system.service.SysViewTabService;
import com.lihua.utils.security.JwtUtils;
import com.lihua.utils.security.LoginUserMgmt;
import com.lihua.utils.tree.TreeUtils;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

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
    private SysMenuMapper sysMenuMapper;

    @Resource
    private SysPostMapper sysPostMapper;

    @Resource
    private SysDeptMapper sysDeptMapper;


    @Resource
    private LihuaConfig lihuaConfig;

    @Transactional
    @Override
    public String login(CurrentUser currentUser) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(currentUser.getUsername(), currentUser.getPassword()));
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        // 处理登录用户信息，将用户基本数据存入 LoginUser 后存入 redis
        cacheUserLoginDetails(loginUser);
        // 根据username 生成jwt 返回
        return JwtUtils.create(loginUser.getUser().getId());
    }

    /**
     * 根据业务需要，可将用户数据存入 LoginUser 业务中可直接获取使用
     * @param loginUser
     */
    @Override
    public void cacheUserLoginDetails(LoginUser loginUser) {
        String id = loginUser.getUser().getId();
        // 菜单router信息
        List<CurrentRouter> routerList = sysMenuService.selectSysMenuByLoginUserId(id);
        // 角色信息
        List<CurrentRole> roles = sysRoleMapper.selectSysRoleByUserId(id);
        // 权限信息
        List<CurrentRouter> permList = sysMenuMapper.selectPermsByUserId(id);
        // 收藏/固定菜单
        List<CurrentViewTab> viewTabList = sysViewTabService.selectByUserId(id);
        // 岗位信息
        List<CurrentPost> postList = sysPostMapper.selectByUserId(id);
        // 部门信息
        List<CurrentDept> deptList = sysDeptMapper.selectByUserId(id);

        // viewTab赋值routerPathKey
        handleSetViewTabKey(viewTabList,routerList);
        // 处理角色权限信息
        List<String> authorities = handleAuthorities(roles,permList);

        loginUser
            .setRouterList(routerList)
            .setRoleList(roles)
            .setViewTabList(viewTabList)
            .setDeptList(deptList)
            .setDeptTree(TreeUtils.buildTree(deptList))
            .setPostList(postList)
            .setAuthorities(authorities);

        // 设置redis缓存
        LoginUserMgmt.setLoginUserCache(loginUser);
    }

    private void handleSetViewTabKey(List<CurrentViewTab> currentViewTabList,List<CurrentRouter> routerList) {
        currentViewTabList.forEach(tab -> {
            routerList.forEach(item -> {
                if (item.getId().equals(tab.getMenuId())) {
                    tab.setRouterPathKey(item.getKey());
                }
                if (item.getChildren() != null && !item.getChildren().isEmpty()) {
                    handleSetViewTabKey(currentViewTabList,item.getChildren());
                }
            });
        });

    }

    /**
     * spring security 默认将RULE_ 开头的字符串认定为角色，其余认定为权限；都存放在 GrantedAuthority 中
     * @param roleList
     * @param routerList
     * @return
     */
    private List<String> handleAuthorities(List<CurrentRole> roleList,List<CurrentRouter> routerList) {
        // 过滤出用户权限信息
        List<String> perms = new ArrayList<>(routerList.stream()
                .map(CurrentRouter::getPerms)
                .filter(StringUtils::hasText)
                .distinct()
                .toList());

        // 过滤出用户角色信息
        List<String> roleCodes = roleList.stream()
                .map(CurrentRole::getCode)
                .filter(StringUtils::hasText)
                .distinct()
                .toList();
        // 合并角色/权限
        perms.addAll(roleCodes);
        return perms;
    }
}
