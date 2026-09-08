package com.lihua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.lihua.common.utils.date.DateUtils;
import com.lihua.entity.SysUser;
import com.lihua.entity.SysUserNotice;
import com.lihua.mapper.SysUserNoticeMapper;
import com.lihua.model.dto.NoticeReadInfoDTO;
import com.lihua.security.manager.LoginUserContext;
import com.lihua.service.SysUserNoticeService;
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
    public List<String> queryUserIds(String noticeId) {
        QueryWrapper<SysUserNotice> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SysUserNotice::getNoticeId, noticeId)
                .select(SysUserNotice::getUserId);
        List<SysUserNotice> sysUserNotices = sysUserNoticeMapper.selectList(queryWrapper);
        return sysUserNotices.stream().map(SysUserNotice::getUserId).toList();
    }

    @Override
    public IPage<SysUser> queryReadInfo(NoticeReadInfoDTO readInfoDTO) {
        return sysUserNoticeMapper.queryReadInfo(new Page<>(readInfoDTO.getPageNum(), readInfoDTO.getPageSize()),
                readInfoDTO.getNoticeId(), readInfoDTO.getReadFlag());
    }

    @Override
    public void resetStatus(String noticeId) {
        UpdateWrapper<SysUserNotice> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .eq(SysUserNotice::getNoticeId, noticeId)
                .set(SysUserNotice::getStarFlag, "0")
                .set(SysUserNotice::getReadFlag, "0")
                .set(SysUserNotice::getReadTime, null);
        sysUserNoticeMapper.update(updateWrapper);
    }

    @Override
    public void changeStar(String noticeId, String star) {
        UpdateWrapper<SysUserNotice> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(SysUserNotice::getStarFlag, star)
                        .eq(SysUserNotice::getNoticeId, noticeId)
                        .eq(SysUserNotice::getUserId, LoginUserContext.getUserId());
        sysUserNoticeMapper.update(updateWrapper);
    }

    @Override
    public void changeRead(String noticeId) {
        UpdateWrapper<SysUserNotice> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .set(SysUserNotice::getReadFlag, "1")
                .set(SysUserNotice::getReadTime, DateUtils.now())
                .eq(SysUserNotice::getNoticeId, noticeId)
                .eq(SysUserNotice::getUserId, LoginUserContext.getUserId());
        sysUserNoticeMapper.update(updateWrapper);
    }

    @Override
    public int queryUnReadCount() {
        return sysUserNoticeMapper.queryUnReadCount(LoginUserContext.getUserId());
    }

}
