package com.lihua.service.system.viewtab;

import com.lihua.model.security.CurrentRouter;
import com.lihua.model.security.CurrentViewTab;
import com.lihua.entity.system.SysViewTab;

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
