package com.lihua.strategy.cacheloginuser;

import com.lihua.mapper.SysDeptMapper;
import com.lihua.security.model.CurrentDept;
import com.lihua.security.model.LoginUserSession;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 缓存部门相关实现类
 */
@Component
public class CacheDeptStrategyImpl implements CacheLoginUserStrategy {

    @Resource
    private SysDeptMapper sysDeptMapper;

    @Override
    public void cacheLoginUser(LoginUserSession loginUserSession, boolean isAdmin) {
        String id = loginUserSession.getUser().getId();
        List<CurrentDept> deptList;
        if (isAdmin) {
            deptList = sysDeptMapper.selectAllDept(id);
        } else {
            deptList = sysDeptMapper.selectByUserId(id);
        }
        loginUserSession.setDeptList(deptList);
    }
}
