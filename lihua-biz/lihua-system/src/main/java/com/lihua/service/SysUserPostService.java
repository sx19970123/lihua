package com.lihua.service;

import com.lihua.entity.SysUserPost;
import com.lihua.model.vo.SysPostVO;

import java.util.Collection;
import java.util.List;

public interface SysUserPostService {

    /**
     * 保存关联数据
     */
    void save(List<SysUserPost> sysUserPosts);

    /**
     * 根据用户id删除关联数据
     */
    void deleteByUserIds(List<String> userIds);

    /**
     * 根据用户id集合查询用户下对应的岗位信息
     */
    List<SysPostVO> queryPostByUserIds(Collection<String> userIds);


}
