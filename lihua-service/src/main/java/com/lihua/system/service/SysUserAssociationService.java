package com.lihua.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.system.entity.SysUser;
import com.lihua.system.model.SysUserAssociationDTO;

public interface SysUserAssociationService {

    /**
     * 获取不同业务下的用户列表
     * @param sysUserAssociationDTO
     * @return
     */
    IPage<SysUser> findAssociatedUserPage(SysUserAssociationDTO sysUserAssociationDTO);

    /**
     * 查询与业务没有关联的用户信息
     * @param sysUserAssociationDTO
     * @return
     */
    IPage<SysUser> findUnassociatedUserPage(SysUserAssociationDTO sysUserAssociationDTO);

    /**
     * 解除用户和对应业务的绑定
     * @param sysUserAssociationDTO
     */
    void detach(SysUserAssociationDTO sysUserAssociationDTO);

    /**
     * 将用户和对应业务进行绑定
     * @param sysUserAssociationDTO
     */
    void associate(SysUserAssociationDTO sysUserAssociationDTO);
}
