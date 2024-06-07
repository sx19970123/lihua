package com.lihua.system.service;

import com.lihua.system.entity.SysMenu;

import java.util.List;

public interface SysMenuService {


    /**
     * 列表页查询
     * @param sysMenu
     * @return
     */
    List<SysMenu> findList(SysMenu sysMenu);

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

    /**
     * 根据 id 集合删除元素
     * @param ids
     */
    void deleteByIds(List<String> ids);

    /**
     * 获取菜单树Option
     * @return
     */
    List<SysMenu> menuTreeOption();

}
