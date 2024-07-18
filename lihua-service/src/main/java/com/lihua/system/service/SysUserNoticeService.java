package com.lihua.system.service;

import com.lihua.system.entity.SysUserNotice;

import java.util.List;

public interface SysUserNoticeService {

    /**
     * 保存关联表数据
     * @param sysUserNotices
     */
    void save(List<SysUserNotice> sysUserNotices);

    /**
     * 根据 noticeIds 删除关联表数据
     * @param noticeIds
     */
    void deleteByNoticeIds(List<String> noticeIds);

    /**
     * 根据 noticeId 获取全部用户id
     * @param noticeId
     * @return
     */
    List<String> findUserIds(String noticeId);
}
