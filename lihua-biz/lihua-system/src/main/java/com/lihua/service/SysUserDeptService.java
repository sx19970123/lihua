package com.lihua.service;

import com.lihua.entity.SysUserDept;
import com.lihua.security.model.CurrentDept;

import java.util.List;

public interface SysUserDeptService {

    // 保存用户下的部门
    void save(List<SysUserDept> sysUserDeptList);

    // 清空用户下的部门
    void deleteByUserIds(List<String> userIds);

    // 设置默认部门
    CurrentDept setDefaultDept(String deptId);
}
