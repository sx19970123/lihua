package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lihua.system.entity.SysUserPost;
import com.lihua.system.entity.SysUserRole;
import com.lihua.system.mapper.SysUserPostMapper;
import com.lihua.system.service.SysUserPostService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserPostServiceImpl extends ServiceImpl<SysUserPostMapper, SysUserPost> implements SysUserPostService {

    @Override
    public void save(List<SysUserPost> sysUserPosts) {
        saveBatch(sysUserPosts);
    }

    @Override
    public void deleteByUserId(String userId) {
        QueryWrapper<SysUserPost> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysUserPost::getUserId, userId);
        remove(queryWrapper);
    }
}
