package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lihua.system.entity.SysUserPost;
import com.lihua.system.mapper.SysUserPostMapper;
import com.lihua.system.service.SysUserPostService;
import java.util.List;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

@Service
public class SysUserPostServiceImpl extends ServiceImpl<SysUserPostMapper, SysUserPost> implements SysUserPostService {

    @Override
    public void save(List<SysUserPost> sysUserPosts) {
        SysUserPostServiceImpl sysUserPostService = (SysUserPostServiceImpl) AopContext.currentProxy();
        sysUserPostService.saveBatch(sysUserPosts);
    }

    @Override
    public void deleteByUserIds(List<String> userIds) {
        QueryWrapper<SysUserPost> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(SysUserPost::getUserId, userIds);
        remove(queryWrapper);
    }
}
