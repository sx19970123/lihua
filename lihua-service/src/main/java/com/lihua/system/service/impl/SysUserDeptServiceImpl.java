package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lihua.system.entity.SysUserDept;
import com.lihua.system.mapper.SysUserDeptMapper;
import com.lihua.system.service.SysUserDeptService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserDeptServiceImpl extends ServiceImpl<SysUserDeptMapper, SysUserDept> implements SysUserDeptService {

    @Override
    public void save(List<SysUserDept> sysUserDeptList) {
        saveBatch(sysUserDeptList);
    }

    @Override
    public void deleteByUserId(String userId) {
        QueryWrapper<SysUserDept> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysUserDept::getUserId, userId);
        remove(queryWrapper);
    }
}
