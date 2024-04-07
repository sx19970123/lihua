package com.lihua.system.service.impl;

import com.lihua.enums.ResultCodeEnum;
import com.lihua.model.security.LoginUser;
import com.lihua.model.security.RouterVO;
import com.lihua.model.security.SysRoleVO;
import com.lihua.model.security.SysUserVO;
import com.lihua.system.mapper.SysMenuMapper;
import com.lihua.system.mapper.SysRoleMapper;
import com.lihua.system.mapper.SysUserMapper;
import jakarta.annotation.Resource;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoginUserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysMenuMapper sysMenuMapper;
    @Resource
    private SysRoleMapper sysRoleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 查询用户信息
        SysUserVO sysUserVO = sysUserMapper.selectByUsername(username);
        if (sysUserVO == null) {
            throw new UsernameNotFoundException("用户名未找到");
        }
        // 查询用户权限信息
        List<RouterVO> routerVOList = null;
        List<SysRoleVO> sysRoleVOS = null;
        try {
            routerVOList = sysMenuMapper.selectPermsByUserId(sysUserVO.getId());
            sysRoleVOS = sysRoleMapper.selectSysRoleByUserId(sysUserVO.getId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(ResultCodeEnum.getDefaultExceptionMessage(ResultCodeEnum.DB_ERROR));
        }
        if (routerVOList == null || routerVOList.isEmpty()) {
            throw new PermissionDeniedDataAccessException(ResultCodeEnum.getDefaultExceptionMessage(ResultCodeEnum.NO_ACCESS_ERROR) ,null);
        }
        if (sysRoleVOS == null || sysRoleVOS.isEmpty()) {
            throw new PermissionDeniedDataAccessException(ResultCodeEnum.getDefaultExceptionMessage(ResultCodeEnum.NO_ROLE_ERROR) ,null);
        }
        // 过滤出用户权限信息
        List<String> perms = new ArrayList<>(routerVOList.stream()
                .map(RouterVO::getPerms)
                .filter(StringUtils::hasText)
                .distinct()
                .toList());

        // 过滤出用户角色信息
        List<String> roleCodes = sysRoleVOS.stream()
                .map(item -> {
                    item.setCode("ROLE_" + item.getCode());
                    return item.getCode();
                })
                .filter(StringUtils::hasText)
                .distinct()
                .toList();

        // 合并角色/权限
        perms.addAll(roleCodes);

        if (perms.isEmpty()) {
            throw new PermissionDeniedDataAccessException(ResultCodeEnum.getDefaultExceptionMessage(ResultCodeEnum.NO_PERMISSION_ERROR) ,null);
        }

        return new LoginUser(sysUserVO,perms);
    }
}

