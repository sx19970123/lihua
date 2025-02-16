package com.lihua.service.system.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lihua.entity.system.SysUserRole;
import com.lihua.mapper.system.SysUserRoleMapper;
import com.lihua.service.system.user.SysUserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

    @Override
    public void save(List<SysUserRole> sysUserRoleList) {
        saveBatch(sysUserRoleList);
    }

    @Override
    public void deleteByUserIds(List<String> userIds) {
        QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(SysUserRole::getUserId, userIds);
        remove(queryWrapper);
    }
}
