package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lihua.model.security.LoginUser;
import com.lihua.model.security.SysUserVO;
import com.lihua.system.entity.SysUser;
import com.lihua.system.mapper.SysUserMapper;
import com.lihua.system.service.SysUserService;
import com.lihua.utils.security.LoginUserContext;
import com.lihua.utils.security.LoginUserMgmt;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public void saveTheme(String theme) {
        UpdateWrapper<SysUser> updateWrapper = new UpdateWrapper<>();
        LoginUser loginUser = LoginUserContext.getLoginUser();
        SysUserVO sysUserVO = loginUser.getSysUserVO();
        updateWrapper.lambda().eq(SysUser::getId,sysUserVO.getId())
                .set(SysUser::getTheme,theme)
                .set(SysUser::getUpdateTime, LocalDateTime.now());
        int update = sysUserMapper.update(updateWrapper);
        if (update == 1) {
            sysUserVO.setTheme(theme);
            LoginUserMgmt.setLoginUserCache(loginUser);
        }
    }
}
