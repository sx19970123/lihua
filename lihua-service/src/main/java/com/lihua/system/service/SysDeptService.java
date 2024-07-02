package com.lihua.system.service;

import com.lihua.system.entity.SysDept;
import com.lihua.system.model.vo.SysDeptVO;

import java.util.List;

public interface SysDeptService {

    /**
     * 查询列表
     * @param sysDept
     * @return
     */
    List<SysDept> findList(SysDept sysDept);


    /**
     * 查询带有岗位信息的post列表
     * @param sysDept
     * @return
     */
    List<SysDeptVO> findDeptPostList(SysDept sysDept);

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

    /**
     * 修改部门状态
     * @param id
     * @param currentStatus
     * @return
     */
    String updateStatus(String id, String currentStatus);
}
