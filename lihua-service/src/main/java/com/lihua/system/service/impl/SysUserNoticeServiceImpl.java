package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lihua.system.entity.SysUser;
import com.lihua.system.entity.SysUserNotice;
import com.lihua.system.mapper.SysUserNoticeMapper;
import com.lihua.system.service.SysUserNoticeService;
import com.lihua.system.service.SysUserService;
import com.lihua.utils.date.DateUtils;
import com.lihua.utils.security.LoginUserContext;
import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

@Service
public class SysUserNoticeServiceImpl extends ServiceImpl<SysUserNoticeMapper, SysUserNotice> implements SysUserNoticeService {

    @Resource
    private SysUserNoticeMapper sysUserNoticeMapper;

    @Resource
    private SysUserService sysUserService;

    @Override
    public void save(List<SysUserNotice> sysUserNotices) {
        SysUserNoticeServiceImpl noticeService = (SysUserNoticeServiceImpl) AopContext.currentProxy();
        noticeService.saveBatch(sysUserNotices);
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

    @Override
    public Map<String, List<SysUser>> findReadInfo(String noticeId) {
        Map<String, List<SysUser>> resultMap = new HashMap<>();

        // 查询用户消息关联表
        QueryWrapper<SysUserNotice> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SysUserNotice::getNoticeId, noticeId)
                .select(SysUserNotice::getUserId, SysUserNotice::getReadFlag);
        List<SysUserNotice> sysUserNotices = sysUserNoticeMapper.selectList(queryWrapper);

        // 根据已读未读分组获取分别获取用户id
        Map<String, List<SysUserNotice>> groupByReadFlag = sysUserNotices.stream().collect(Collectors.groupingBy(SysUserNotice::getReadFlag));
        List<String> unReadIds = groupByReadFlag.getOrDefault("0", new ArrayList<>()).stream().map(SysUserNotice::getUserId).collect(Collectors.toList());
        List<String> readIds = groupByReadFlag.getOrDefault("1", new ArrayList<>()).stream().map(SysUserNotice::getUserId).collect(Collectors.toList());

        // 分别获取已读未读用户信息
        if (!unReadIds.isEmpty()) {
            resultMap.put("0", sysUserService.userOption(unReadIds));
        }
        if (!readIds.isEmpty()) {
            resultMap.put("1", sysUserService.userOption(readIds));
        }

        return resultMap;
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
    public int findUnReadCount() {
        return sysUserNoticeMapper.findUnReadCount(LoginUserContext.getUserId());
    }

}
