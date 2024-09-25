package com.lihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lihua.exception.ServiceException;
import com.lihua.model.security.CurrentUser;
import com.lihua.model.security.LoginUser;
import com.lihua.system.entity.SysUser;
import com.lihua.system.mapper.SysUserMapper;
import com.lihua.system.service.SysProfileService;
import com.lihua.utils.date.DateUtils;
import com.lihua.utils.security.LoginUserContext;
import com.lihua.utils.security.LoginUserManager;
import com.lihua.utils.security.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SysProfileServiceImpl implements SysProfileService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public String saveBasics(SysUser sysUser) {
        // 获取当前登陆信息
        CurrentUser currentUser = LoginUserContext.getLoginUser().getUser();
        // 验证手机号码、邮箱
        checkPhoneNumber(sysUser.getPhoneNumber(),currentUser.getId());
        checkEmailNumber(sysUser.getEmail(),currentUser.getId());

        // 修改
        UpdateWrapper<SysUser> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .eq(SysUser::getId,currentUser.getId())
                .set(SysUser::getAvatar,sysUser.getAvatar())
                .set(SysUser::getNickname,sysUser.getNickname())
                .set(SysUser::getPhoneNumber,sysUser.getPhoneNumber())
                .set(SysUser::getEmail,sysUser.getEmail())
                .set(SysUser::getGender,sysUser.getGender())
                .set(SysUser::getUpdateTime, DateUtils.now())
                .set(SysUser::getUpdateId,currentUser.getId());

        int update = sysUserMapper.update(updateWrapper);
        // 更新缓存
        if (update == 1) {
            currentUser.setAvatar(sysUser.getAvatar());
            currentUser.setEmail(sysUser.getEmail());
            currentUser.setPhoneNumber(sysUser.getPhoneNumber());
            currentUser.setGender(sysUser.getGender());
            currentUser.setNickname(sysUser.getNickname());
            LoginUser loginUser = LoginUserContext.getLoginUser();
            loginUser.setUser(currentUser);
            LoginUserManager.setLoginUserCache(loginUser);
        }

        return currentUser.getId();
    }

    @Override
    public String updatePassword(String newPassword) {
        UpdateWrapper<SysUser> updateWrapper = new UpdateWrapper<>();
        LoginUser loginUser = LoginUserContext.getLoginUser();
        LocalDateTime now = LocalDateTime.now();
        CurrentUser currentUser = loginUser.getUser();
        String password = SecurityUtils.encryptPassword(newPassword);
        updateWrapper.lambda().eq(SysUser::getId,currentUser.getId())
                .set(SysUser::getPassword, password)
                .set(SysUser::getUpdateTime, now)
                .set(SysUser::getPasswordUpdateTime, now);

        int update = sysUserMapper.update(updateWrapper);
        // 更新缓存
        if (update == 1) {
            currentUser.setPassword(password);
            currentUser.setPasswordUpdateTime(now);
            LoginUserManager.setLoginUserCache(loginUser);
        }
        return currentUser.getId();
    }

    @Override
    public String getPassword() {
        return sysUserMapper.selectById(LoginUserContext.getUserId()).getPassword();
    }

    @Override
    public String saveTheme(String theme) {
        UpdateWrapper<SysUser> updateWrapper = new UpdateWrapper<>();
        LoginUser loginUser = LoginUserContext.getLoginUser();
        CurrentUser currentUser = loginUser.getUser();
        updateWrapper.lambda().eq(SysUser::getId,currentUser.getId())
                .set(SysUser::getTheme,theme)
                .set(SysUser::getUpdateTime, LocalDateTime.now());
        int update = sysUserMapper.update(updateWrapper);
        if (update == 1) {
            currentUser.setTheme(theme);
            LoginUserManager.setLoginUserCache(loginUser);
        }
        return currentUser.getId();
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
        throw new ServiceException("当前邮箱已存在");
    }

}
