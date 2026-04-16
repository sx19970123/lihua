package com.lihua.strategy.cacheloginuser;

import com.lihua.mapper.SysRoleMapper;
import com.lihua.security.model.CurrentRole;
import com.lihua.security.model.LoginUserSession;
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
    public void cacheLoginUser(LoginUserSession loginUserSession, boolean isAdmin) {
        String id = loginUserSession.getUser().getId();

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
        List<String> permissionList = loginUserSession.getPermissionList();
        if (permissionList == null) {
            loginUserSession.setPermissionList(authorities);
        } else {
            permissionList.addAll(authorities);
            loginUserSession.setPermissionList(permissionList);
        }
        loginUserSession.setRoleList(roleList);
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
