package com.lihua.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.system.model.dto.SysLogDTO;
import com.lihua.system.model.vo.SysLogVO;

import java.util.List;

public interface SysLogService {

    /**
     * 保存日志到数据库
     */
    void insert(SysLogVO sysLogVO);

    /**
     * 查询日志列表
     */
    IPage<? extends SysLogVO> findPage(SysLogDTO sysLogDTO);

    /**
     * 根据主键查询日志
     */
    SysLogVO findById(String id);

    /**
     * 根据缓存查询日志
     */
    SysLogVO findByCacheKey(String cacheKey);

    /**
     * 导出 excel
     */
    String exportExcel(SysLogDTO sysLogDTO);

    /**
     * 根据 id 删除日志
     */
    void deleteByIds(List<String> ids);

    /**
     * 清空日志
     */
    void clearLog();
}
