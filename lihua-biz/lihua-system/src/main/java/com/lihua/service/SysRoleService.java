package com.lihua.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.entity.SysRole;
import com.lihua.model.dto.SysRoleDTO;
import com.lihua.model.dto.SysRoleUserDTO;
import com.lihua.model.vo.SysRoleUserVO;

import java.util.List;

public interface SysRoleService {

    /**
     * 分页查询
     */
    IPage<SysRole> queryPage(SysRoleDTO sysRoleDTO);

    /**
     * 根据id查询角色
     */
    SysRole queryById(String id);

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

    /**
     * 分页查询角色已授权用户
     */
    IPage<SysRoleUserVO> queryUserPage(String roleId, SysRoleUserDTO sysRoleUserDTO);

    /**
     * 批量授权用户
     */
    void saveUsers(String roleId, List<String> userIds);

    /**
     * 批量取消授权用户
     */
    void deleteUsers(String roleId, List<String> userIds);
}
