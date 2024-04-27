package com.lihua.system.service;


import com.lihua.system.entity.SysPost;

import java.util.List;

public interface SysPostService {

    /**
     * 根据部门单位集合查询岗位信息
     * @param deptIdList
     * @return
     */
    List<SysPost> findPostByDeptId(List<String> deptIdList);

}
