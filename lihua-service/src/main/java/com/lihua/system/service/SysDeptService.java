package com.lihua.system.service;

import com.lihua.system.entity.SysDept;

import java.util.List;

public interface SysDeptService {

    /**
     * 查询列表
     * @param sysDept
     * @return
     */
    List<SysDept> findList(SysDept sysDept);

    /**
     * 保存单位/用户
     * @param sysDept
     * @return
     */
    String save(SysDept sysDept);
}
