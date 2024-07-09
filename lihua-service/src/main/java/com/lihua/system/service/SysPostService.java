package com.lihua.system.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.system.entity.SysPost;
import com.lihua.system.model.dto.SysPostDTO;
import com.lihua.system.model.vo.SysPostVO;

import java.util.List;
import java.util.Map;

public interface SysPostService {

    /**
     * 分页查询
     * @param dto
     * @return
     */
    IPage<SysPostVO> findPage(SysPostDTO dto);

    /**
     * 根据id 查询
     * @param id
     * @return
     */
    SysPost findById(String id);

    /**
     * 保存岗位
     * @param sysPost
     * @return
     */
    String save(SysPost sysPost);

    /**
     * 根据主键删除
     * @param ids
     */
    void deleteByIds(List<String> ids);

    /**
     * 根据部门集合查询岗位信息
     * @param deptIdList
     * @return
     */
    List<SysPost> findPostByDeptId(List<String> deptIdList);

    /**
     * 根据部门几盒查询岗位数零
     * @param deptIdList
     * @return
     */
    Long findCountByDeptId(List<String> deptIdList);

    /**
     * 根据部门id查询岗位集合
     * @param deptIds
     * @return
     */
    Map<String, List<SysPost>> getPostOptionByDeptId(List<String> deptIds);

    /**
     * 修改状态
     * @param id
     * @param currentStatus
     * @return
     */
    String updateStatus(String id, String currentStatus);

    /**
     * excel 导出
     * @param dto
     * @return
     */
    String exportExcel(SysPostDTO dto);
}
