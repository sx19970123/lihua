package com.lihua.system.service;

import com.lihua.system.entity.SysMenu;

import java.util.List;

public interface SysMenuService {


    /**
     * 列表页查询
     */
    List<SysMenu> findList(SysMenu sysMenu);

    /**
     * 根据id查询菜单
     */
    SysMenu findById(String menuId);

    /**
     * 保存菜单
     */
    String save(SysMenu sysMenu);

    /**
     * 根据 id 集合删除元素
     */
    void deleteByIds(List<String> ids);

    /**
     * 获取菜单树Option
     */
    List<SysMenu> menuTreeOption();

    /**
     * 修改菜单状态
     */
    String updateStatus(List<String> ids, String currentStatus);
}
