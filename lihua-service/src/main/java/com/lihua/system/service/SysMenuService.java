package com.lihua.system.service;

import com.lihua.system.model.SysMenuVO;

import java.util.List;

public interface SysMenuService {
    /**
     * 通过当前登录用户获取菜单信息
     * @return
     */
    List<SysMenuVO> selectSysMenuByLoginUser();
}
