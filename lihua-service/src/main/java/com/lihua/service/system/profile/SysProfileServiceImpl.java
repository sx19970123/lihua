package com.lihua.service.system.profile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lihua.exception.ServiceException;
import com.lihua.model.security.CurrentUser;
import com.lihua.model.security.LoginUser;
import com.lihua.entity.system.SysUser;
import com.lihua.mapper.system.SysUserMapper;
import com.lihua.utils.date.DateUtils;
import com.lihua.utils.security.LoginUserContext;
import com.lihua.utils.security.LoginUserManager;
import com.lihua.utils.security.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

        // 头像
        if (StringUtils.hasText(sysUser.getAvatar())) {
            updateWrapper.lambda().set(SysUser::getAvatar,sysUser.getAvatar());
        }
        // 昵称
        if (StringUtils.hasText(sysUser.getNickname())) {
            updateWrapper.lambda().set(SysUser::getNickname,sysUser.getNickname());
        }
        // 手机号码
        if (sysUser.getPhoneNumber() != null) {
            updateWrapper.lambda().set(SysUser::getPhoneNumber,sysUser.getPhoneNumber());
        }
        // 邮箱
        if (sysUser.getEmail() != null) {
            updateWrapper.lambda().set(SysUser::getEmail,sysUser.getEmail());
        }
        // 性别
        if (StringUtils.hasText(sysUser.getGender())) {
            updateWrapper.lambda().set(SysUser::getGender,sysUser.getGender());
        }

        // 更新时间/更新人
        updateWrapper.lambda().eq(SysUser::getId,currentUser.getId())
                .set(SysUser::getUpdateTime, DateUtils.now())
                .set(SysUser::getUpdateId,currentUser.getId());

        // 执行更新
        int update = sysUserMapper.update(updateWrapper);

        // 更新缓存
        if (update == 1) {
            // 头像
            if (StringUtils.hasText(sysUser.getAvatar())) {
                currentUser.setAvatar(sysUser.getAvatar());
            }
            // 昵称
            if (StringUtils.hasText(sysUser.getNickname())) {
                currentUser.setNickname(sysUser.getNickname());
            }
            // 手机号码
            if (sysUser.getPhoneNumber() != null) {
                currentUser.setPhoneNumber(sysUser.getPhoneNumber());
            }
            // 邮箱
            if (sysUser.getEmail() != null) {
                currentUser.setEmail(sysUser.getEmail());
            }
            // 性别
            if (StringUtils.hasText(sysUser.getGender())) {
                currentUser.setGender(sysUser.getGender());
            }
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
        LocalDateTime now = DateUtils.now();
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
                .set(SysUser::getUpdateTime, DateUtils.now());
        int update = sysUserMapper.update(updateWrapper);
        if (update == 1) {
            currentUser.setTheme(theme);
            LoginUserManager.setLoginUserCache(loginUser);
        }
        return currentUser.getId();
    }


    /**
     * 验证手机号
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
