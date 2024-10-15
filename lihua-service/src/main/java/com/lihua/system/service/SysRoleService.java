package com.lihua.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.system.entity.SysRole;
import com.lihua.system.model.dto.SysRoleDTO;

import java.util.List;

public interface SysRoleService {

    /**
     * 分页查询
     */
    IPage<SysRole> findPage(SysRoleDTO sysRoleDTO);

    /**
     * 根据id查询角色
     */
    SysRole findById(String id);

    /**
     * 保存角色信息
     */
    String save(SysRole sysRole);

    /**
     * 根据id集合删除角色
     */
    void deleteByIds(List<String> ids);


    /**
     * 修改状态
     * @param id 角色id
     * @param currentStatus 当前状态
     * @return 更新后状态
     */
    String updateStatus(String id, String currentStatus);
}
