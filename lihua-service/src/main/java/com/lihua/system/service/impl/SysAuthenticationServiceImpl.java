package com.lihua.system.service.impl;

import com.lihua.cache.RedisCache;
import com.lihua.model.security.*;
import com.lihua.system.entity.SysSetting;
import com.lihua.system.entity.SysUser;
import com.lihua.system.mapper.*;
import com.lihua.system.model.dto.SysSettingDTO;
import com.lihua.system.service.*;
import com.lihua.utils.json.JsonUtils;
import com.lihua.utils.security.JwtUtils;
import com.lihua.utils.security.LoginUserManager;
import com.lihua.utils.security.SecurityUtils;
import com.lihua.utils.tree.TreeUtils;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
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

    @Resource
    private SysSettingService sysSettingService;

    @Resource
    private SysUserMapper sysUserMapper;


    private final String patternComponentName =  "([^/]+)\\.vue$";

    @Override
    public Map<String, String> login(CurrentUser currentUser) {
        Map<String, String> map = new HashMap<>();
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(currentUser.getUsername(), currentUser.getPassword()));
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        // 处理登录用户信息，将用户基本数据存入 LoginUser 后存入 redis
        cacheUserLoginDetails(loginUser);
        // 检查登录后的必要配置
        List<String> loginSettingComponentNameList = checkLoginSetting(loginUser, currentUser.getPassword());

        // 返回需要配置的组件名称
        if (!loginSettingComponentNameList.isEmpty()) {
            map.put("setting", String.join(",", loginSettingComponentNameList));
        }
        // 返回token
        map.put("token", JwtUtils.create(loginUser.getUser().getId()));
        // 根据username 生成jwt 返回
        return map;
    }

    /**
     * 登录后必要信息校验，对应于前端 components/login-setting 下的组件进行处理
     * LoginSettingResetPassword：登陆后修改密码
     * LoginSettingDefaultDept：登录后选择默认部门
     * @param loginUser
     */
    private List<String> checkLoginSetting(LoginUser loginUser, String password) {
        List<String> loginSettingComponentNameList = new ArrayList<>();

        // 检查密码
        checkUpdatePassword(loginSettingComponentNameList, loginUser, password);
        // 检查默认部门
        checkDefaultDept(loginSettingComponentNameList, loginUser);

        return loginSettingComponentNameList;
    }

    // 判断是否需要修改密码（登录密码与默认密码相同 或 到达管理员配置的修改密码时间）
    public void checkUpdatePassword(List<String> loginSettingComponentNameList, LoginUser loginUser, String password) {

        // 获取默认密码
        SysSetting defaultPasswordSetting = sysSettingService.getSysSettingByComponentName("DefaultPasswordSetting");
        SysSettingDTO.DefaultPasswordSetting passwordSetting = JsonUtils.toObject(defaultPasswordSetting.getSettingJson(), SysSettingDTO.DefaultPasswordSetting.class);
        if (passwordSetting.getDefaultPassword().equals(password)) {
            loginSettingComponentNameList.add("LoginSettingResetPassword");
            return;
        }

        // 获取定期修改密码配置
        SysSetting intervalUpdatePasswordSetting = sysSettingService.getSysSettingByComponentName("IntervalUpdatePasswordSetting");
        SysSettingDTO.IntervalUpdatePasswordSetting updatePasswordSetting = JsonUtils.toObject(intervalUpdatePasswordSetting.getSettingJson(), SysSettingDTO.IntervalUpdatePasswordSetting.class);



        Boolean enable = updatePasswordSetting.isEnable();

        if (!enable) {
            return;
        }

        // 更新周期
        Integer interval = updatePasswordSetting.getInterval();

        // 周期单位
        String unit = updatePasswordSetting.getUnit();

        // 上次更新密码时间
        LocalDateTime passwordUpdateTime = loginUser.getUser().getPasswordUpdateTime();

        LocalDateTime targetTime = null;
        switch (unit) {
            case "day": {
                targetTime = passwordUpdateTime.plusDays(interval);
                break;
            }
            case "week": {
                targetTime = passwordUpdateTime.plusWeeks(interval);
                break;
            }
            case "month": {
                targetTime = passwordUpdateTime.plusMonths(interval);
                break;
            }
            case "year": {
                targetTime = passwordUpdateTime.plusYears(interval);
                break;
            }
        }

        // 当前时间在目标时间之后，需要修改密码
       if ( LocalDateTime.now().isAfter(targetTime)) {
           loginSettingComponentNameList.add("LoginSettingResetPassword");
       }
    }

    // 判断是否存在默认部门
    private void checkDefaultDept(List<String> loginSettingComponentNameList, LoginUser loginUser) {
        List<CurrentDept> deptList = loginUser.getDeptList();
        List<CurrentDept> defDeptList = deptList.stream().filter(item -> "0".equals(item.getDefaultDept())).toList();
        if (defDeptList.isEmpty()) {
            loginSettingComponentNameList.add("LoginSettingDefaultDept");
        }
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
        LoginUserManager.setLoginUserCache(loginUser);
    }

    @Override
    public boolean checkUserName(String username) {
        return sysUserMapper.checkUserName(username) == null;
    }

    @Override
    @Transactional
    public String register(String username, String password) {
        // 用户注册
        SysUser sysUser = new SysUser();
        sysUser.setUsername(username);
        sysUser.setPassword(SecurityUtils.encryptPassword(password));
        sysUser.setCreateTime(LocalDateTime.now());
        sysUser.setDelFlag("0");
        sysUser.setStatus("0");
        sysUserMapper.insert(sysUser);

        // todo 根据管理员配置，插入角色/部门关联
        return sysUser.getId();
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
        handleRouterPathKey(routerList, "");
        return routerList;
    }

    // 处理 routerPathKey
    private void handleRouterPathKey(List<CurrentRouter> routerList, String parentKey) {
        for (CurrentRouter item : routerList) {
            String key = item.getPath().startsWith("/") ? item.getPath() : "/" + item.getPath();
            // 根据菜单层级关系设置key
            if ("0".equals(item.getParentId())) {
                item.setKey(key);
            } else {
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
