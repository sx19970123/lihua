package com.lihua.service;

import com.lihua.entity.SysUserPost;

import java.util.List;

public interface SysUserPostService {

    void save(List<SysUserPost> sysUserPosts);

    void deleteByUserIds(List<String> userIds);


}
