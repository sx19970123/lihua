package com.lihua.system.service.impl;

import com.lihua.enums.ResultCodeEnum;
import com.lihua.model.security.LoginUser;
import com.lihua.model.security.RouterVO;
import com.lihua.model.security.SysUserVO;
import com.lihua.system.mapper.SysMenuMapper;
import com.lihua.system.mapper.SysUserMapper;
import jakarta.annotation.Resource;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class LoginUserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysMenuMapper sysMenuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 查询用户信息
        SysUserVO sysUserVO = sysUserMapper.selectByUsername(username);
        if (sysUserVO == null) {
            throw new UsernameNotFoundException("用户名未找到");
        }
        // 查询用户权限信息
        List<RouterVO> routerVOList = null;
        try {
            routerVOList = sysMenuMapper.selectPermsByUserId(sysUserVO.getId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(ResultCodeEnum.getDefaultExceptionMessage(ResultCodeEnum.DB_ERROR));
        }
        if (routerVOList == null || routerVOList.isEmpty()) {
            throw new PermissionDeniedDataAccessException(ResultCodeEnum.getDefaultExceptionMessage(ResultCodeEnum.NO_ACCESS_ERROR) ,null);
        }
        // 过滤出用户权限信息
        List<String> perms = routerVOList.stream()
                .map(RouterVO::getPerms)
                .filter(StringUtils::hasText)
                .distinct()
                .toList();

        if (perms.isEmpty()) {
            throw new PermissionDeniedDataAccessException(ResultCodeEnum.getDefaultExceptionMessage(ResultCodeEnum.NO_ACCESS_ERROR) ,null);
        }

        return new LoginUser(sysUserVO,perms);
    }
}

