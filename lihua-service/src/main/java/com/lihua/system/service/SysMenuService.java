package com.lihua.system.service;

import com.lihua.model.RouteVO;
import com.lihua.model.SysMenuVO;

import java.util.List;

public interface SysMenuService {
    /**
     * 通过用户 id 获取菜单信息
     * @return
     */
    List<SysMenuVO> selectSysMenuByLoginUserId(String userId);


    /**
     * 通过菜单数据生成 vue router 元数据
     * @return
     */
    List<RouteVO> initMetaRouteInfo(String userId);
}
