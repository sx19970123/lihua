package com.lihua.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lihua.system.entity.SysOperateLog;
import org.apache.ibatis.annotations.Delete;

public interface SysOperateLogMapper extends BaseMapper<SysOperateLog> {
    @Delete("DELETE FROM sys_operate_log")
    void clear();
}
