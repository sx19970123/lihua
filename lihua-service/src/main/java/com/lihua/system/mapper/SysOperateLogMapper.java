package com.lihua.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lihua.system.entity.SysOperateLog;
import org.apache.ibatis.annotations.Delete;

public interface SysOperateLogMapper extends BaseMapper<SysOperateLog> {
    // 清空操作日志数据
    void clear();
}
