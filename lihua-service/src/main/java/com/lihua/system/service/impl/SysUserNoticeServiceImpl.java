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
    public void deleteByNoticeIds(List<String> noticeIds) {
        QueryWrapper<SysUserNotice> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(SysUserNotice::getNoticeId, noticeIds);
        remove(queryWrapper);
    }

    @Override
    public List<String> findUserIds(String noticeId) {
        QueryWrapper<SysUserNotice> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SysUserNotice::getNoticeId, noticeId)
                .select(SysUserNotice::getUserId);
        List<SysUserNotice> sysUserNotices = sysUserNoticeMapper.selectList(queryWrapper);
        return sysUserNotices.stream().map(SysUserNotice::getUserId).toList();
    }
}
