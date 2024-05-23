package com.lihua.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.model.security.SysUserVO;
import com.lihua.system.model.SysUserDTO;

import java.util.List;

public interface SysUserService {

    /**
     * 用户列表页查询
     * @param sysUserDTO
     * @return
     */
    IPage<SysUserVO> findPage(SysUserDTO sysUserDTO);

    /**
     * 根据id查询用户信息
     * @return
     */
    SysUserVO findById(String id);

    /**
     * 用户信息保存
     * @return
     */
    String save(SysUserDTO sysUserDTO);

    /**
     * 删除用户信息
     * @return
     */
    void deleteByIds(List<String> ids);
}
