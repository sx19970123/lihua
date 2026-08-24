package com.lihua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lihua.entity.SysUserRole;
import com.lihua.mapper.SysUserRoleMapper;
import com.lihua.service.SysUserRoleService;
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
