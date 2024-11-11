package com.lihua.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lihua.system.entity.SysUserNotice;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface SysUserNoticeMapper extends BaseMapper<SysUserNotice> {
    // 根据用户id查询未读通知数量
    int findUnReadCount(@Param("userId") String userId);
}
