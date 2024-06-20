package com.lihua.system.service;

import com.lihua.system.entity.SysUserPost;

import java.util.List;

public interface SysUserPostService {

    void save(List<SysUserPost> sysUserPosts);

    void deleteByUserIds(List<String> userIds);


}
