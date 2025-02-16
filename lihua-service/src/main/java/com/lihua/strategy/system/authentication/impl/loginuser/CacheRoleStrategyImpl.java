package com.lihua.strategy.system.authentication.impl.loginuser;

import com.lihua.model.security.CurrentRole;
import com.lihua.model.security.LoginUser;
import com.lihua.mapper.system.SysRoleMapper;
import com.lihua.strategy.system.authentication.CacheLoginUserStrategy;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 缓存角色相关实现类
 */
@Component
public class CacheRoleStrategyImpl implements CacheLoginUserStrategy {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Override
    public void cacheLoginUser(LoginUser loginUser, boolean isAdmin) {
        String id = loginUser.getUser().getId();

        List<CurrentRole> roleList;
        if (isAdmin) {
            roleList = sysRoleMapper.selectAllRole();
        }
        else {
            roleList = sysRoleMapper.selectSysRoleByUserId(id);
        }
        // 处理权限
        List<String> authorities = getAuthorities(roleList);
        // 设置权限
        List<String> permissionList = loginUser.getPermissionList();
        if (permissionList == null) {
            loginUser.setPermissionList(authorities);
        } else {
            permissionList.addAll(authorities);
            loginUser.setPermissionList(permissionList);
        }
        loginUser.setRoleList(roleList);
    }

    // 获取roleList中的角色数据
    private List<String> getAuthorities(List<CurrentRole> roleList) {
        return roleList.stream()
                .map(CurrentRole::getCode)
                .filter(StringUtils::hasText)
                .distinct()
                .collect(Collectors.toList());
    }
}
