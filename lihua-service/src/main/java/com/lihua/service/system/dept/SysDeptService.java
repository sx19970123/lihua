package com.lihua.service.system.dept;

import com.lihua.model.excel.ExcelImportResult;
import com.lihua.entity.system.SysDept;
import com.lihua.model.system.vo.SysDeptVO;

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
    String exportExcel(SysDept sysDept);

    /**
     * excel 导入
     */
    ExcelImportResult importExcel(List<SysDeptVO> sysUserVOS);
}
