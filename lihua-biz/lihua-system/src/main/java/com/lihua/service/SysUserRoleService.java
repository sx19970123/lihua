package com.lihua.service;

import com.lihua.entity.SysUserRole;

import java.util.List;

public interface SysUserRoleService {

    // 批量插入
    void save(List<SysUserRole> sysUserRoleList);

    // 根据userid删除
    void deleteByUserIds(List<String> userIds);

}
