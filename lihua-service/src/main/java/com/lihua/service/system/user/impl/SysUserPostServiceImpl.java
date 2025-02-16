package com.lihua.service.system.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lihua.entity.system.SysUserPost;
import com.lihua.mapper.system.SysUserPostMapper;
import com.lihua.service.system.user.SysUserPostService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserPostServiceImpl extends ServiceImpl<SysUserPostMapper, SysUserPost> implements SysUserPostService {

    @Override
    public void save(List<SysUserPost> sysUserPosts) {
        saveBatch(sysUserPosts);
    }

    @Override
    public void deleteByUserIds(List<String> userIds) {
        QueryWrapper<SysUserPost> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(SysUserPost::getUserId, userIds);
        remove(queryWrapper);
    }
}
