package com.lihua.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.model.security.SysRoleVO;
import com.lihua.system.entity.SysRole;
import com.lihua.system.model.SysRoleDTO;

import java.util.List;

public interface SysRoleService {

    /**
     * 分页查询
     * @param sysRoleDTO
     * @return
     */
    IPage<SysRole> findPage(SysRoleDTO sysRoleDTO);

    /**
     * 根据id查询角色
     * @param id
     * @return
     */
    SysRole findById(String id);

    /**
     * 保存角色信息
     * @param sysRole
     * @return
     */
    String save(SysRole sysRole);

    /**
     * 根据id集合删除角色
     * @param ids
     */
    void deleteByIds(List<String> ids);

    /**
     * 获取当前用户下的角色信息
     * @return
     */
    List<SysRoleVO> getRoleOption();

}
