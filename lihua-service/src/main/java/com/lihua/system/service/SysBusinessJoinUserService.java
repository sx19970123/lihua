package com.lihua.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.system.entity.SysUser;
import com.lihua.system.model.SysBusinessUserDTO;

public interface SysBusinessJoinUserService {

    /**
     * 获取不同业务下的用户列表
     * @param sysBusinessUserDTO
     * @return
     */
    IPage<SysUser> findBusinessUserPage(SysBusinessUserDTO sysBusinessUserDTO);

    /**
     * 查询与业务没有关联的用户信息
     * @param sysBusinessUserDTO
     * @return
     */
    IPage<SysUser> findNotBusinessUserPage(SysBusinessUserDTO sysBusinessUserDTO);

    /**
     * 解除用户和对应业务的绑定
     * @param sysBusinessUserDTO
     */
    void detach(SysBusinessUserDTO sysBusinessUserDTO);

    /**
     * 将用户和对应业务进行绑定
     * @param sysBusinessUserDTO
     */
    void associate(SysBusinessUserDTO sysBusinessUserDTO);
}
