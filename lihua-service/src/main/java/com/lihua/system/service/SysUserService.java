package com.lihua.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.system.model.SysUserDTO;
import com.lihua.system.model.SysUserVO;

import java.io.File;
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

    /**
     * 修改用户状态
     * @param id 用户id
     * @param currentStatus 当前用户状态
     * @return 更新后的用户状态
     */
    String updateStatus(String id, String currentStatus);

    /**
     * 导出excel
     * @param sysUserDTO
     * @return
     */
    File exportExcel(SysUserDTO sysUserDTO);
}
