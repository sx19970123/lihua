package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lihua.exception.ServiceException;
import com.lihua.model.security.LoginUser;
import com.lihua.model.security.SysUserVO;
import com.lihua.system.entity.SysUser;
import com.lihua.system.mapper.SysUserMapper;
import com.lihua.system.service.SysUserService;
import com.lihua.utils.date.DateUtils;
import com.lihua.utils.security.LoginUserContext;
import com.lihua.utils.security.LoginUserMgmt;
import com.lihua.utils.security.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public String saveBasics(SysUser sysUser) {
        // 获取当前登陆信息
        SysUserVO sysUserVO = LoginUserContext.getLoginUser().getSysUserVO();
        // 验证手机号码、邮箱
        checkPhoneNumber(sysUser.getPhoneNumber(),sysUserVO.getId());
        checkEmailNumber(sysUser.getEmail(),sysUserVO.getId());

        // 修改
        UpdateWrapper<SysUser> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .eq(SysUser::getId,sysUserVO.getId())
                .set(SysUser::getAvatar,sysUser.getAvatar())
                .set(SysUser::getPhoneNumber,sysUser.getPhoneNumber())
                .set(SysUser::getEmail,sysUser.getEmail())
                .set(SysUser::getGender,sysUser.getGender())
                .set(SysUser::getUpdateTime, DateUtils.now())
                .set(SysUser::getUpdateId,sysUserVO.getId());

        int update = sysUserMapper.update(updateWrapper);
        // 更新缓存
        if (update == 1) {
            sysUserVO.setAvatar(sysUser.getAvatar());
            sysUserVO.setEmail(sysUser.getEmail());
            sysUserVO.setPhoneNumber(sysUser.getPhoneNumber());
            sysUserVO.setGender(sysUser.getGender());
            LoginUser loginUser = LoginUserContext.getLoginUser();
            loginUser.setSysUserVO(sysUserVO);
            LoginUserMgmt.setLoginUserCache(loginUser);
        }

        return sysUserVO.getId();
    }

    @Override
    public String updatePassword(String newPassword) {
        UpdateWrapper<SysUser> updateWrapper = new UpdateWrapper<>();
        LoginUser loginUser = LoginUserContext.getLoginUser();
        SysUserVO sysUserVO = loginUser.getSysUserVO();
        String password = SecurityUtils.encryptPassword(newPassword);
        updateWrapper.lambda().eq(SysUser::getId,sysUserVO.getId())
                .set(SysUser::getPassword, password)
                .set(SysUser::getUpdateTime, LocalDateTime.now());

        int update = sysUserMapper.update(updateWrapper);
        if (update == 1) {
            sysUserVO.setPassword(password);
            LoginUserMgmt.setLoginUserCache(loginUser);
        }
        return sysUserVO.getId();
    }

    @Override
    public String saveTheme(String theme) {
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
        return sysUserVO.getId();
    }


    /**
     * 验证手机号
     * @param phoneNumber
     */
    private void checkPhoneNumber(String phoneNumber,String userId) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysUser::getPhoneNumber,phoneNumber);
        List<SysUser> sysUsers = sysUserMapper.selectList(queryWrapper);
        if (sysUsers.isEmpty()) {
            return;
        }
        if (sysUsers.get(0).getId().equals(userId)) {
            return;
        }
        throw new ServiceException("当前手机号码已存在");
    }

    /**
     * 验证邮箱
     * @param email
     */
    private void checkEmailNumber(String email,String userId) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysUser::getEmail,email);
        List<SysUser> sysUsers = sysUserMapper.selectList(queryWrapper);
        if (sysUsers.isEmpty()) {
            return;
        }
        if (sysUsers.get(0).getId().equals(userId)) {
            return;
        }
        throw new ServiceException("当前手机号码已存在");
    }

}
