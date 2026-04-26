package com.lihua.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lihua.entity.SysOperateLog;

public interface SysOperateLogMapper extends BaseMapper<SysOperateLog> {
    // 清空操作日志数据
    void clear();
}
