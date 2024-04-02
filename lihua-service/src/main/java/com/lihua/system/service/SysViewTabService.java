package com.lihua.system.service;

import com.lihua.model.security.SysViewTabVO;
import com.lihua.system.entity.SysViewTab;

import java.util.List;

public interface SysViewTabService {
    /**
     * 根据用户id查询viewTab数据
     * @param userId
     * @return
     */
    List<SysViewTabVO> selectByUserId(String userId);

    /**
     * 保存viewTab收藏数据
     * @param sysViewTab
     * @return
     */
    SysViewTabVO save(SysViewTab sysViewTab);
}
