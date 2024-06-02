package com.lihua.system.service;

import com.lihua.system.entity.SysUserDept;

import java.util.List;

public interface SysUserDeptService {

    void save(List<SysUserDept> sysUserDeptList);

    void deleteByUserId(String userId);


}
