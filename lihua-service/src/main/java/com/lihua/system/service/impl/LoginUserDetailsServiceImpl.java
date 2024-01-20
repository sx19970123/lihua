package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lihua.enums.ResultCodeEnum;
import com.lihua.model.LoginUser;
import com.lihua.entity.SysUser;
import com.lihua.system.mapper.SysMenuMapper;
import com.lihua.system.mapper.SysUserMapper;
import com.lihua.model.SysMenuVO;
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
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysUser::getUsername,username);
        // 查询用户信息
        SysUser sysUser = sysUserMapper.selectOne(wrapper);
        if (sysUser == null) {
            throw new UsernameNotFoundException("用户名未找到");
        }
        // 查询用户权限信息
        List<SysMenuVO> menus = sysMenuMapper.selectPermsByUserId(sysUser.getId());
        if (menus == null || menus.isEmpty()) {
            throw new PermissionDeniedDataAccessException(ResultCodeEnum.getDefaultExceptionMessage(ResultCodeEnum.NO_ACCESS_ERROR) ,null);
        }
        List<String> perms = menus.stream()
                .map(SysMenuVO::getPerms)
                .filter(StringUtils::hasText)
                .distinct()
                .toList();

        if (perms.isEmpty()) {
            throw new PermissionDeniedDataAccessException(ResultCodeEnum.getDefaultExceptionMessage(ResultCodeEnum.NO_ACCESS_ERROR) ,null);
        }

        return new LoginUser(sysUser,perms);
    }
}

