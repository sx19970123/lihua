package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lihua.system.entity.SysUserRole;
import com.lihua.system.mapper.SysUserRoleMapper;
import com.lihua.system.service.SysUserRoleService;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

    @Override
    public void save(List<SysUserRole> sysUserRoleList) {
        SysUserRoleServiceImpl sysUserRoleService = (SysUserRoleServiceImpl) AopContext.currentProxy();
        sysUserRoleService.saveBatch(sysUserRoleList);
    }

    @Override
    public void deleteByUserIds(List<String> userIds) {
        QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(SysUserRole::getUserId, userIds);
        remove(queryWrapper);
    }
}
