package com.lihua.system.service;

import com.lihua.model.security.CurrentViewTab;
import com.lihua.system.entity.SysViewTab;

import java.util.List;

public interface SysViewTabService {
    /**
     * 根据用户id查询viewTab数据
     * @param userId
     * @return
     */
    List<CurrentViewTab> selectByUserId(String userId);

    /**
     * 保存viewTab收藏数据
     * @param sysViewTab
     * @return
     */
    CurrentViewTab save(SysViewTab sysViewTab);
}
