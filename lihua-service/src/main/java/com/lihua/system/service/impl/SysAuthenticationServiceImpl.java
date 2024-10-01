package com.lihua.system.service.impl;

import com.lihua.cache.RedisCache;
import com.lihua.enums.SysBaseEnum;
import com.lihua.exception.ServiceException;
import com.lihua.model.security.*;
import com.lihua.system.entity.*;
import com.lihua.system.mapper.*;
import com.lihua.system.model.dto.SysSettingDTO;
import com.lihua.system.service.*;
import com.lihua.utils.json.JsonUtils;
import com.lihua.utils.security.JwtUtils;
import com.lihua.utils.security.LoginUserManager;
import com.lihua.utils.security.SecurityUtils;
import com.lihua.utils.tree.TreeUtils;
import com.lihua.utils.web.WebUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
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

    @Resource
    private SysUserRoleService sysUserRoleService;

    @Resource
    private SysUserPostService sysUserPostService;

    @Resource
    private SysUserDeptService sysUserDeptService;

    @Resource
    private RedisCache<String> redisCache;

    private final String patternComponentName =  "([^/]+)\\.vue$";

    @Override
    public LoginUser login(CurrentUser currentUser) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(currentUser.getUsername(), currentUser.getPassword()));
        return  (LoginUser) authenticate.getPrincipal();
    }


    @Override
    public String checkLoginSetting(LoginUser loginUser, String password) {
        List<String> loginSettingComponentNameList = new ArrayList<>();

        // 检查密码
        checkUpdatePassword(loginSettingComponentNameList, loginUser, password);
        // 检查是否为自助注册并首次登录用户
        checkNewUserBasicInfo(loginSettingComponentNameList, loginUser);
        // 检查默认部门
        checkDefaultDept(loginSettingComponentNameList, loginUser);

        // 将对应组件名称处理为逗号分割返回
        String loginSettingComponentName = null;
        if (!loginSettingComponentNameList.isEmpty()) {
            loginSettingComponentName = String.join(",", loginSettingComponentNameList);
        }

        return loginSettingComponentName;
    }


    // 判断是否需要完善用户信息
    private void checkNewUserBasicInfo(List<String> loginSettingComponentNameList, LoginUser loginUser) {
        String registerType = loginUser.getUser().getRegisterType();

        if ("0".equals(registerType)) {
            return;
        }

        if (loginUser.getUser().getNickname() == null && loginUser.getUser().getGender() == null) {
            loginSettingComponentNameList.add("LoginSettingUserBasics");
        }
    }

    // 判断是否需要修改密码（登录密码与默认密码相同 或 到达管理员配置的修改密码时间）
    public void checkUpdatePassword(List<String> loginSettingComponentNameList, LoginUser loginUser, String password) {

        // 获取默认密码
        SysSetting defaultPasswordSetting = sysSettingService.getSysSettingByComponentName("DefaultPasswordSetting");
        SysSettingDTO.DefaultPasswordSetting passwordSetting = JsonUtils.toObject(defaultPasswordSetting.getSettingJson(), SysSettingDTO.DefaultPasswordSetting.class);
        if (SecurityUtils.matchesPassword(passwordSetting.getDefaultPassword(), password)) {
            loginSettingComponentNameList.add("LoginSettingResetPassword");
            return;
        }

        // 获取定期修改密码配置
        SysSetting intervalUpdatePasswordSetting = sysSettingService.getSysSettingByComponentName("IntervalUpdatePasswordSetting");
        SysSettingDTO.IntervalUpdatePasswordSetting updatePasswordSetting = JsonUtils.toObject(intervalUpdatePasswordSetting.getSettingJson(), SysSettingDTO.IntervalUpdatePasswordSetting.class);

        boolean enable = updatePasswordSetting.isEnable();

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


    @Override
    public String cacheLoginUserInfo(LoginUser loginUser) {
        String id = loginUser.getUser().getId();
        // 缓存中隐藏密码
        loginUser.getUser().setPassword(null);
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
        // 当前请求 httpServletRequest
        HttpServletRequest httpServletRequest = WebUtils.getCurrentRequest();
        loginUser
            .setRouterList(routerList)
            .setRoleList(roleList)
            .setViewTabList(viewTabList)
            .setDeptList(deptList)
            .setDeptTree(TreeUtils.buildTree(new ArrayList<>(deptList)))
            .setPostList(postList)
            .setIpAddress(httpServletRequest.getRemoteAddr())
            .setAuthorities(authorities);

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
        return sysUserMapper.checkUserName(username) == null;
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

        LocalDateTime now = LocalDateTime.now();

        // 用户注册
        SysUser sysUser = new SysUser();
        sysUser.setUsername(username);
        sysUser.setPassword(SecurityUtils.encryptPassword(password));
        sysUser.setCreateTime(now);
        sysUser.setDelFlag("0");
        sysUser.setStatus("0");
        sysUser.setRegisterType("1");
        sysUser.setPasswordUpdateTime(now);

        sysUserMapper.insert(sysUser);

        // 用户角色关联表
        List<String> roleIds = signInSetting.getRoleIds();
        if (!roleIds.isEmpty()) {
            List<SysUserRole> sysUserRoles = new ArrayList<>(roleIds.size());
            roleIds.forEach(roleId -> sysUserRoles.add(new SysUserRole(sysUser.getId(), roleId, now, null)));
            sysUserRoleService.save(sysUserRoles);
        }

        // 用户部门关联表
        List<String> deptIds = signInSetting.getDeptIds();
        if (!deptIds.isEmpty()) {
            String defaultDeptId = signInSetting.getDefaultDeptId();
            List<SysUserDept> sysUserDeptList  = new ArrayList<>(deptIds.size());
            deptIds.forEach(deptId -> sysUserDeptList.add(new SysUserDept(sysUser.getId(), deptId, now, null, deptId.equals(defaultDeptId) ? "0" : "1")));
            sysUserDeptService.save(sysUserDeptList);
        }

        // 用户岗位关联表
        List<String> postIds = signInSetting.getPostIds();
        if (!postIds.isEmpty()) {
            List<SysUserPost> sysUserPosts  = new ArrayList<>(postIds.size());
            postIds.forEach(postId -> sysUserPosts.add(new SysUserPost(sysUser.getId(), postId, now, null)));
            sysUserPostService.save(sysUserPosts);
        }

        return sysUser.getId();
    }

    @Override
    public void checkSameAccount(String token) {
        String s = redisCache.memoryInfo();
        System.out.println(s);
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
