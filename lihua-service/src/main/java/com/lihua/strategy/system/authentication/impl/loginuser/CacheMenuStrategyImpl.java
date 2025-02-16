package com.lihua.strategy.system.authentication.impl.loginuser;

import com.lihua.model.security.CurrentRouter;
import com.lihua.model.security.CurrentViewTab;
import com.lihua.model.security.LoginUser;
import com.lihua.mapper.system.SysMenuMapper;
import com.lihua.service.system.viewtab.SysViewTabService;
import com.lihua.strategy.system.authentication.CacheLoginUserStrategy;
import com.lihua.utils.tree.TreeUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 缓存菜单相关实现类
 */
@Component
public class CacheMenuStrategyImpl implements CacheLoginUserStrategy {

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Resource
    private SysViewTabService sysViewTabService;

    private final String patternComponentName =  "([^/]+)\\.vue$";

    @Override
    public void cacheLoginUser(LoginUser loginUser, boolean isAdmin) {
        String id = loginUser.getUser().getId();

        // 菜单/权限信息
        List<CurrentRouter> menuList;

        if (isAdmin) {
            menuList = sysMenuMapper.selectAllPerms();
        } else {
            menuList = sysMenuMapper.selectPermsByUserId(id);
        }

        // 收藏/固定菜单
        List<CurrentViewTab> viewTabList = sysViewTabService.selectByUserId(id,menuList);
        // 处理菜单router信息
        List<CurrentRouter> routerList = handleSysMenu(menuList);
        // viewTab赋值routerPathKey
        handleSetViewTabKey(viewTabList,routerList);
        // 处理权限
        List<String> authorities = getAuthorities(menuList);
        // 设置权限
        List<String> permissionList = loginUser.getPermissionList();
        if (permissionList == null) {
            loginUser.setPermissionList(authorities);
        } else {
            permissionList.addAll(authorities);
            loginUser.setPermissionList(permissionList);
        }

        loginUser
                .setRouterList(routerList)
                .setViewTabList(viewTabList);
    }

    // 处理menu数据为路由数据
    private List<CurrentRouter> handleSysMenu(List<CurrentRouter> currentRouterList) {
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

    // 处理ViewTab
    private void handleSetViewTabKey(List<CurrentViewTab> currentViewTabList,List<CurrentRouter> routerList) {
        currentViewTabList.forEach(tab -> routerList.forEach(item -> {
            if (item.getId().equals(tab.getMenuId())) {
                tab.setRouterPathKey(item.getKey());
            }
            if (item.getChildren() != null && !item.getChildren().isEmpty()) {
                handleSetViewTabKey(currentViewTabList,item.getChildren());
            }
        }));
    }

    // 获取routerList中的权限数据
    private List<String> getAuthorities(List<CurrentRouter> routerList) {
        return routerList
                .stream()
                .filter(router -> "perms".equals(router.getType()))
                .map(CurrentRouter::getPerms)
                .filter(StringUtils::hasText)
                .distinct()
                .collect(Collectors.toList());
    }
}
