package com.lihua.system.service;

import com.lihua.system.entity.SysUser;
import com.lihua.system.entity.SysUserNotice;

import java.io.File;
import java.util.List;
import java.util.Map;

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

    /**
     * 获取notice已读未读用户
     * @param noticeId
     * @return
     */
    Map<String,List<SysUser>> findReadInfo(String noticeId);

    /**
     * 重制 notice 关联表状态
     * @param noticeId
     */
    void resetStatus(String noticeId);

    /**
     * 用户添加star
     * @param noticeId
     * @param star 0/1
     * @return
     */
    void changeStar(String noticeId, String star);

    /**
     * 用户已读
     * @param noticeId
     */
    void changeRead(String noticeId);

    /**
     * 获取未读消息总数
     * @return
     */
    int findUnReadCount();
}
