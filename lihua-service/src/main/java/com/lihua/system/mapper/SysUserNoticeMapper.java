package com.lihua.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lihua.system.entity.SysUserNotice;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface SysUserNoticeMapper extends BaseMapper<SysUserNotice> {
    @Select("SELECT COUNT(1) FROM sys_notice LEFT JOIN sys_user_notice ON sys_user_notice.notice_id = sys_notice.id WHERE sys_user_notice.user_id = #{userId} AND sys_user_notice.read_flag = '0' AND sys_notice.status = '1' AND sys_notice.del_flag = '0'")
    int findUnReadCount(@Param("userId") String userId);
}
