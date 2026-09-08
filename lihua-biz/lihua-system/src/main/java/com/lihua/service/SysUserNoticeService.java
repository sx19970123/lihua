package com.lihua.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.entity.SysUser;
import com.lihua.entity.SysUserNotice;
import com.lihua.model.dto.NoticeReadInfoDTO;

import java.util.List;

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
     * 分页获取 notice 已读/未读用户（readFlag：0 未读 / 1 已读）
     */
    IPage<SysUser> queryReadInfo(NoticeReadInfoDTO readInfoDTO);

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
