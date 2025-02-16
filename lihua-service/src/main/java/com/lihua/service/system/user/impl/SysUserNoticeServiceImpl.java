package com.lihua.service.system.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lihua.entity.system.SysUser;
import com.lihua.entity.system.SysUserNotice;
import com.lihua.mapper.system.SysUserNoticeMapper;
import com.lihua.service.system.user.SysUserNoticeService;
import com.lihua.service.system.user.SysUserService;
import com.lihua.utils.date.DateUtils;
import com.lihua.utils.security.LoginUserContext;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysUserNoticeServiceImpl extends ServiceImpl<SysUserNoticeMapper, SysUserNotice> implements SysUserNoticeService {

    @Resource
    private SysUserNoticeMapper sysUserNoticeMapper;

    @Resource
    private SysUserService sysUserService;

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
    public Map<String, List<SysUser>> queryReadInfo(String noticeId) {
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
    public int queryUnReadCount() {
        return sysUserNoticeMapper.queryUnReadCount(LoginUserContext.getUserId());
    }

}
