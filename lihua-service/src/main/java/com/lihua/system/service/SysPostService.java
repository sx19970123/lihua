package com.lihua.system.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.system.entity.SysPost;
import com.lihua.system.model.SysPostDTO;
import com.lihua.system.model.SysPostVO;

import java.util.List;

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
}
