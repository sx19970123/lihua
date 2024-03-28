package com.lihua.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.model.security.RouterVO;
import com.lihua.system.entity.SysMenu;
import com.lihua.system.model.SysMenuDTO;

import java.util.List;

public interface SysMenuService {


    /**
     * 列表页查询
     * @param sysMenuDTO
     * @return
     */
    IPage<SysMenu> findPage(SysMenuDTO sysMenuDTO);

    /**
     * 根据id查询菜单
     * @param menuId
     * @return
     */
    SysMenu findById(String menuId);

    /**
     * 保存菜单
     * @param sysMenu
     * @return
     */
    String save(SysMenu sysMenu);

    void deleteByIds(List<String> ids);


    /**
     * 通过用户 id 获取菜单信息
     * @return
     */
    List<RouterVO> selectSysMenuByLoginUserId(String userId);

}
