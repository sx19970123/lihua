package com.lihua.strategy.system.authentication.impl.loginuser;

import com.lihua.model.security.CurrentPost;
import com.lihua.model.security.LoginUser;
import com.lihua.mapper.system.SysPostMapper;
import com.lihua.strategy.system.authentication.CacheLoginUserStrategy;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 缓存岗位相关实现类
 */
@Component
public class CachePostStrategyImpl implements CacheLoginUserStrategy {

    @Resource
    private SysPostMapper sysPostMapper;

    @Override
    public void cacheLoginUser(LoginUser loginUser, boolean isAdmin) {
        String id = loginUser.getUser().getId();
        // 岗位信息
        List<CurrentPost> postList;

        if (isAdmin) {
            postList = sysPostMapper.selectAllPost();
        } else {
            postList = sysPostMapper.selectByUserId(id);
        }

        loginUser.setPostList(postList);
    }
}
