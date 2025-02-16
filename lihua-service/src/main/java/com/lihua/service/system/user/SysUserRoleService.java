package com.lihua.service.system.user;

import com.lihua.entity.system.SysUserRole;

import java.util.List;

public interface SysUserRoleService {

    // 批量插入
    void save(List<SysUserRole> sysUserRoleList);

    // 根据userid删除
    void deleteByUserIds(List<String> userIds);

}
