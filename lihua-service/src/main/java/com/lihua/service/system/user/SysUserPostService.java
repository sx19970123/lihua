package com.lihua.service.system.user;

import com.lihua.entity.system.SysUserPost;

import java.util.List;

public interface SysUserPostService {

    void save(List<SysUserPost> sysUserPosts);

    void deleteByUserIds(List<String> userIds);


}
