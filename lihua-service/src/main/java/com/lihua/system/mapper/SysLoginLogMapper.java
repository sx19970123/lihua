package com.lihua.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lihua.system.entity.SysLoginLog;
import org.apache.ibatis.annotations.Delete;

public interface SysLoginLogMapper extends BaseMapper<SysLoginLog> {
    @Delete("DELETE FROM sys_login_log")
    void clear();
}
