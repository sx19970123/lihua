package com.lihua.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lihua.entity.SysUser;
import com.lihua.entity.SysUserNotice;
import org.apache.ibatis.annotations.Param;

public interface SysUserNoticeMapper extends BaseMapper<SysUserNotice> {
    // 根据用户id查询未读通知数量
    int queryUnReadCount(@Param("userId") String userId);

    // 分页查询 notice 已读/未读用户
    IPage<SysUser> queryReadInfo(Page<SysUser> page, @Param("noticeId") String noticeId, @Param("readFlag") String readFlag);
}
