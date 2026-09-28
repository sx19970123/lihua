package com.lihua.strategy.cacheloginuser;

import com.lihua.mapper.SysPostMapper;
import com.lihua.security.model.CurrentPost;
import com.lihua.security.model.LoginUserSession;
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
    public void cacheLoginUser(LoginUserSession loginUserSession, boolean isAdmin) {
        String id = loginUserSession.getUser().getId();
        // 岗位信息
        List<CurrentPost> postList;

        if (isAdmin) {
            postList = sysPostMapper.selectAllPost();
        } else {
            postList = sysPostMapper.selectByUserId(id);
        }

        loginUserSession.setPostList(postList);
    }
}
