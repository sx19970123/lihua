package com.lihua.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.entity.SysPost;
import com.lihua.model.dto.SysPostDTO;
import com.lihua.model.vo.SysPostVO;

import java.util.List;
import java.util.Map;

public interface SysPostService {

    /**
     * 分页查询
     */
    IPage<SysPostVO> queryPage(SysPostDTO dto);

    /**
     * 根据id查询
     */
    SysPost queryById(String id);

    /**
     * 保存岗位
     */
    String savePost(SysPost sysPost);

    /**
     * 根据主键删除
     */
    void deleteByIds(List<String> ids);

    /**
     * 根据部门集合查询岗位信息
     */
    List<SysPost> queryPostByDeptId(List<String> deptIdList);

    /**
     * 根据部门几盒查询岗位数零
     */
    Long queryCountByDeptId(List<String> deptIdList);

    /**
     * 根据部门id查询岗位集合
     */
    Map<String, List<SysPost>> getPostOptionByDeptId(List<String> deptIds);

    /**
     * 修改状态
     */
    String updateStatus(String id, String currentStatus);

    /**
     * excel 导出
     */
    List<SysPostVO> exportExcel(SysPostDTO dto);
}
