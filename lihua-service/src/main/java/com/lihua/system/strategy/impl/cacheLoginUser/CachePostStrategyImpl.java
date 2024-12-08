package com.lihua.system.strategy.impl.cacheLoginUser;

import com.lihua.model.security.CurrentPost;
import com.lihua.model.security.LoginUser;
import com.lihua.system.mapper.SysPostMapper;
import com.lihua.system.strategy.CacheLoginUserStrategy;
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
