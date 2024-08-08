package com.lihua.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.system.model.dto.SysLogDTO;
import com.lihua.system.model.vo.SysLogVO;

public interface SysLogService {

    /**
     * 插入日志
     * @param sysLogVO
     * @return
     */
    String insert(SysLogVO sysLogVO);

    /**
     * 查询日志列表
     * @param sysLogDTO
     * @return
     */
    IPage<? extends SysLogVO> findPage(SysLogDTO sysLogDTO);

    /**
     * 根据主键查询日志
     * @return
     */
    SysLogVO findById(String id);
}
