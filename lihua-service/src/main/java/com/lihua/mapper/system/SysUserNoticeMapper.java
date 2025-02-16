package com.lihua.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lihua.entity.system.SysUserNotice;
import org.apache.ibatis.annotations.Param;

public interface SysUserNoticeMapper extends BaseMapper<SysUserNotice> {
    // 根据用户id查询未读通知数量
    int queryUnReadCount(@Param("userId") String userId);
}
