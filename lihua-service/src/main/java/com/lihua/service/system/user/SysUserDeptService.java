package com.lihua.service.system.user;

import com.lihua.model.security.CurrentDept;
import com.lihua.entity.system.SysUserDept;

import java.util.List;

public interface SysUserDeptService {

    // 保存用户下的部门
    void save(List<SysUserDept> sysUserDeptList);

    // 清空用户下的部门
    void deleteByUserIds(List<String> userIds);

    // 判断登陆用户是否属于该部门
    boolean hasDept(String deptId);

    // 设置默认部门
    CurrentDept setDefaultDept(String deptId);
}
