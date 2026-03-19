package com.lihua.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.common.model.bridge.log.LogModel;
import com.lihua.model.dto.SysLogDTO;
import com.lihua.model.vo.SysLogVO;

import java.util.List;

public interface SysLogService {

    /**
     * 保存日志到数据库
     */
    void insert(LogModel logModel);

    /**
     * 查询日志列表
     */
    IPage<? extends SysLogVO> queryPage(SysLogDTO sysLogDTO);

    /**
     * 根据主键查询日志
     */
    SysLogVO queryById(String id);

    /**
     * 根据缓存查询日志
     */
    SysLogVO queryByCacheKey(String cacheKey);

    /**
     * 导出 excel
     */
    List<? extends SysLogVO> exportExcel(SysLogDTO sysLogDTO);

    /**
     * 根据 id 删除日志
     */
    void deleteByIds(List<String> ids);

    /**
     * 清空日志
     */
    void clearLog();
}
