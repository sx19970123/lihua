package com.lihua.service;

import com.lihua.entity.SysViewTab;
import com.lihua.security.model.CurrentRouter;
import com.lihua.security.model.CurrentViewTab;

import java.util.List;

public interface SysViewTabService {
    /**
     * 根据用户id查询viewTab数据
     */
    List<CurrentViewTab> selectByUserId(String userId, List<CurrentRouter> routerVOList);

    /**
     * 保存viewTab收藏数据
     */
    CurrentViewTab save(SysViewTab sysViewTab);
}
