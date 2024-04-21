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
     * 保存单位
     * @param sysDept
     * @return
     */
    String save(SysDept sysDept);

    /**
     * 根据id查询单位
     * @param id
     * @return
     */
    SysDept findById(String id);


    /**
     * 根据ids 删除单位部门
     * @param ids
     */
    void deleteByIds(List<String> ids);


    /**
     * 获取部门树option
     * @return
     */
    List<SysDept> deptTreeOption();
}
