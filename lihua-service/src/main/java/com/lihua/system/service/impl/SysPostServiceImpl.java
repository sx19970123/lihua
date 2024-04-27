package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lihua.system.entity.SysPost;
import com.lihua.system.mapper.SysPostMapper;
import com.lihua.system.service.SysPostService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysPostServiceImpl implements SysPostService {

    @Resource
    private SysPostMapper sysPostMapper;

    @Override
    public List<SysPost> findPostByDeptId(List<String> deptIdList) {
        QueryWrapper<SysPost> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .in(SysPost::getDeptId,deptIdList)
                .orderByAsc(SysPost::getSort);
        return sysPostMapper.selectList(queryWrapper);
    }
}
