package com.lihua.service;

import com.lihua.entity.SysDept;
import com.lihua.model.vo.SysDeptVO;

import java.util.List;

public interface SysDeptService {

    /**
     * 查询列表
     */
    List<SysDept> queryList(SysDept sysDept);


    /**
     * 查询带有岗位信息的post列表
     */
    List<SysDeptVO> queryDeptPostList(SysDept sysDept);

    /**
     * 保存单位
     */
    String saveDept(SysDept sysDept);

    /**
     * 根据id查询单位
     */
    SysDept queryById(String id);

    /**
     * 查询给定 id 集合中未删除的 id（全局逻辑删除自动滤 del_flag）
     */
    List<String> queryExistIds(List<String> ids);


    /**
     * 根据ids 删除单位部门
     */
    void deleteByIds(List<String> ids);


    /**
     * 获取部门树option
     */
    List<SysDept> deptTreeOption();

    /**
     * 修改部门状态
     */
    String updateStatus(String id, String currentStatus);

    /**
     * 导出excel
     */
    List<SysDeptVO> exportExcel(SysDept sysDept);
}
