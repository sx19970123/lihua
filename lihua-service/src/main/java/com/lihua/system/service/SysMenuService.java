package com.lihua.system.service;

import com.lihua.model.security.RouterVO;

import java.util.List;

public interface SysMenuService {
    /**
     * 通过用户 id 获取菜单信息
     * @return
     */
    List<RouterVO> selectSysMenuByLoginUserId(String userId);

}
