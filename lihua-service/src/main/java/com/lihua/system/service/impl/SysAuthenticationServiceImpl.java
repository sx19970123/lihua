package com.lihua.system.service.impl;

import com.lihua.cache.RedisCache;
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
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    private final String patternComponentName =  "([^/]+)\\.vue$";

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
        // 角色信息
        boolean isAdmin = isAdmin(id);

        // 菜单/权限信息
        List<CurrentRouter> menuList;
        // 岗位信息
        List<CurrentPost> postList;
        // 部门信息
        List<CurrentDept> deptList;
        // 角色信息
        List<CurrentRole> roleList;
        // admin 查询全部用户信息
        if (isAdmin) {
            menuList = sysMenuMapper.selectAllPerms();
            postList = sysPostMapper.selectAllPost();
            deptList = sysDeptMapper.selectAllDept(id);
            roleList = sysRoleMapper.selectAllRole();
        }
        // 其他用户根据配置权限进行查询
        else {
            menuList = sysMenuMapper.selectPermsByUserId(id);
            postList = sysPostMapper.selectByUserId(id);
            deptList = sysDeptMapper.selectByUserId(id);
            roleList = sysRoleMapper.selectSysRoleByUserId(id);
        }

        // 收藏/固定菜单
        List<CurrentViewTab> viewTabList = sysViewTabService.selectByUserId(id,menuList);
        // 处理菜单router信息
        List<CurrentRouter> routerList = handleSysMenu(menuList);
        // viewTab赋值routerPathKey
        handleSetViewTabKey(viewTabList,routerList);
        // 处理角色权限信息
        List<String> authorities = handleAuthorities(roleList,menuList);

        loginUser
            .setRouterList(routerList)
            .setRoleList(roleList)
            .setViewTabList(viewTabList)
            .setDeptList(deptList)
            .setDeptTree(TreeUtils.buildTree(new ArrayList<>(deptList)))
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
     * 判断当前登录用户是否为超级管理员
     * @param userId
     * @return
     */
    private boolean isAdmin(String userId) {
        List<String> roleCodes = sysRoleMapper.selectCodeByUserId(userId);
        return roleCodes.contains("ROLE_admin");
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
                .filter(router -> "perms".equals(router.getType()))
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


    /**
     * 处理menu数据为路由数据
     * @param currentRouterList
     * @return
     */
    public List<CurrentRouter> handleSysMenu(List<CurrentRouter> currentRouterList) {
        // 不需要权限数据
        currentRouterList = currentRouterList
                .stream()
                .filter(vo -> !vo.getType().equals("perms"))
                .peek(vo -> {
                    // 使用正则表达式从组件路径中获取组件名称
                    String component = vo.getComponent();
                    if (component != null) {
                        Pattern pattern = Pattern.compile(patternComponentName);
                        Matcher matcher = pattern.matcher(component);
                        if (matcher.find()) {
                            String name = matcher.group(1);
                            if (StringUtils.hasText(name)) {
                                vo.setName(name);
                            }
                        }
                    }
                })
                .collect(Collectors.toList());
        // 递归构建树
        List<CurrentRouter> routerList = TreeUtils.buildTree(currentRouterList);
        // 设置层级key，再通过key设置path
        handleRouterPathKey(routerList, null);
        return routerList;
    }

    // 处理 routerPathKey
    private void handleRouterPathKey(List<CurrentRouter> routerList, String parentKey) {
        for (CurrentRouter item : routerList) {
            String key = item.getPath().startsWith("/") ? item.getPath() : "/" + item.getPath();
            // 根据菜单层级关系设置key
            if ("0".equals(item.getParentId())) {
                item.setKey(key);
            } else if (parentKey != null){
                item.setKey(parentKey + key);
            }
            // 设置path
            item.setPath(item.getKey());
            // 存在子集继续递归
            if (item.getChildren() != null && !item.getChildren().isEmpty()) {
                handleRouterPathKey(item.getChildren(),item.getKey());
            }
        }
    }
}
