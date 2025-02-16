package com.lihua.service.system.user;

import com.lihua.entity.system.SysUser;
import com.lihua.entity.system.SysUserNotice;

import java.util.List;
import java.util.Map;

public interface SysUserNoticeService {

    /**
     * 保存关联表数据
     */
    void save(List<SysUserNotice> sysUserNotices);

    /**
     * 根据 noticeIds 删除关联表数据
     */
    void deleteByNoticeIds(List<String> noticeIds);

    /**
     * 根据 noticeId 获取全部用户id
     */
    List<String> queryUserIds(String noticeId);

    /**
     * 获取notice已读未读用户
     */
    Map<String,List<SysUser>> queryReadInfo(String noticeId);

    /**
     * 重制 notice 关联表状态
     */
    void resetStatus(String noticeId);

    /**
     * 用户添加star
     * @param star 0/1
     */
    void changeStar(String noticeId, String star);

    /**
     * 用户已读
     */
    void changeRead(String noticeId);

    /**
     * 获取未读消息总数
     */
    int queryUnReadCount();
}
