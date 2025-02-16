package com.lihua.service.system.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lihua.exception.ServiceException;
import com.lihua.model.security.CurrentDept;
import com.lihua.model.security.LoginUser;
import com.lihua.entity.system.SysUserDept;
import com.lihua.mapper.system.SysDeptMapper;
import com.lihua.mapper.system.SysUserDeptMapper;
import com.lihua.service.system.user.SysUserDeptService;
import com.lihua.utils.date.DateUtils;
import com.lihua.utils.security.LoginUserContext;
import com.lihua.utils.security.LoginUserManager;
import jakarta.annotation.Resource;

import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SysUserDeptServiceImpl extends ServiceImpl<SysUserDeptMapper, SysUserDept> implements SysUserDeptService {

    @Resource
    private SysUserDeptMapper sysUserDeptMapper;

    @Resource
    private SysDeptMapper sysDeptMapper;


    @Override
    public void save(List<SysUserDept> sysUserDeptList) {
        saveBatch(sysUserDeptList);
    }


    @Override
    public void deleteByUserIds(List<String> userIds) {
        QueryWrapper<SysUserDept> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(SysUserDept::getUserId, userIds);
        remove(queryWrapper);
    }

    @Override
    public boolean hasDept(String deptId) {
        return lambdaQuery()
            .eq(SysUserDept::getDeptId, deptId)
            .eq(SysUserDept::getUserId, LoginUserContext.getUserId())
            .exists();
    }

    @Override
    public CurrentDept setDefaultDept(String deptId) {
        LoginUser loginUser = LoginUserContext.getLoginUser();
        String userId = loginUser.getUser().getId();

        // 超级管理员将关联表数据删除后再新增
        if (LoginUserContext.isAdmin()) {
            deleteByUserIds(Collections.singletonList(userId));
            save(List.of(new SysUserDept(userId, deptId, DateUtils.now(), userId, "0")));
        } else {
            // 普通用户修改关联表
            if (!hasDept(deptId)) {
                throw new ServiceException("设置失败，该用户无此部门，请联系管理员");
            }

            // 取消该用户所有默认部门
            lambdaUpdate()
                .eq(SysUserDept::getUserId, LoginUserContext.getUserId())
                .set(SysUserDept::getDefaultDept, "1")
                .update();
            // 设置默认部门
            lambdaUpdate()
                .eq(SysUserDept::getUserId, LoginUserContext.getUserId())
                .eq(SysUserDept::getDeptId, deptId)
                .set(SysUserDept::getDefaultDept, "0")
                .update();
        }

        resetLoginUserDeptCache();

        return LoginUserContext.getDefaultDept();
    }

    // 重新设置部门相关redis缓存
    private void resetLoginUserDeptCache() {
        LoginUser loginUser = LoginUserContext.getLoginUser();
        // 部门信息
        List<CurrentDept> deptList;
        String userId = LoginUserContext.getUserId();
        if (LoginUserContext.isAdmin()) {
            deptList = sysDeptMapper.selectAllDept(userId);
        } else {
            deptList = sysDeptMapper.selectByUserId(userId);
        }
        loginUser.setDeptList(deptList);

        LoginUserManager.setLoginUserCache(loginUser);
    }
}
