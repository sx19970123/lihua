package com.lihua.system.strategy.impl.cacheLoginUser;

import com.lihua.model.security.CurrentDept;
import com.lihua.model.security.LoginUser;
import com.lihua.system.mapper.SysDeptMapper;
import com.lihua.system.strategy.CacheLoginUserStrategy;
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
    public void cacheLoginUser(LoginUser loginUser, boolean isAdmin) {
        String id = loginUser.getUser().getId();
        List<CurrentDept> deptList;
        if (isAdmin) {
            deptList = sysDeptMapper.selectAllDept(id);
        } else {
            deptList = sysDeptMapper.selectByUserId(id);
        }
        loginUser.setDeptList(deptList);
    }
}
