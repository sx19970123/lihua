package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lihua.system.entity.SysUserNotice;
import com.lihua.system.mapper.SysUserNoticeMapper;
import com.lihua.system.service.SysUserNoticeService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserNoticeServiceImpl extends ServiceImpl<SysUserNoticeMapper, SysUserNotice> implements SysUserNoticeService {

    @Resource
    private SysUserNoticeMapper sysUserNoticeMapper;

    @Override
    public void save(List<SysUserNotice> sysUserNotices) {
        saveBatch(sysUserNotices);
    }

    @Override
    public void deleteByUserIds(String noticeId) {
        QueryWrapper<SysUserNotice> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysUserNotice::getNoticeId, noticeId);
        remove(queryWrapper);
    }
}
