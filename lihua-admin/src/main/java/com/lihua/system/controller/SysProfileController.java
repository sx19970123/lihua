package com.lihua.system.controller;

import com.lihua.annotation.Log;
import com.lihua.enums.LogTypeEnum;
import com.lihua.enums.ResultCodeEnum;
import com.lihua.model.web.BaseController;
import com.lihua.system.entity.SysUser;
import com.lihua.system.model.dto.SysUpdatePasswordDTO;
import com.lihua.system.model.validation.ProfileValidation;
import com.lihua.system.service.SysProfileService;
import com.lihua.system.service.SysSettingService;
import com.lihua.system.service.SysUserDeptService;
import com.lihua.utils.security.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;

@RestController
@RequestMapping("system/profile")
public class SysProfileController extends BaseController {

    @Resource
    private SysProfileService sysProfileService;

    @Resource
    private SysUserDeptService sysUserDeptService;

    @Resource
    private SysSettingService sysSettingService;

    /**
     * 保存基础信息
     */
    @PostMapping("basics")
    @Log(description = "保存个人信息", type = LogTypeEnum.SAVE)
    public String saveBasics(@RequestBody @Validated(ProfileValidation.ProfileSaveValidation.class) SysUser sysUser) {
        return success(sysProfileService.saveBasics(sysUser));
    }

    /**
     * 保存主题
     */
    @PostMapping("theme")
    public String saveTheme(@RequestBody @Validated(ProfileValidation.ProfileThemeValidation.class) SysUser sysUser) {
        return success(sysProfileService.saveTheme(sysUser.getTheme()));
    }

    /**
     * 获取图片用户头像
     */
    @GetMapping("avatar")
    public ResponseEntity<StreamingResponseBody> getAvatar(@RequestParam("fullAvatarPath") String fullAvatarPath) {
        return success(new File(fullAvatarPath));
    }

    /**
     * 修改密码
     */
    @PostMapping("password")
    @Log(description = "修改密码", type = LogTypeEnum.SAVE, excludeParams = {"oldPassword", "newPassword", "confirmPassword" ,"oldPasswordRequestKey", "newPasswordRequestKey", "confirmPasswordRequestKey"})
    public String updatePassword(@RequestBody @Validated SysUpdatePasswordDTO sysUpdatePasswordDTO) {
        // 解密旧密码、新密码、确认密码
        String oldPassword = SecurityUtils.decryptGetPassword(sysUpdatePasswordDTO.getOldPassword(), sysUpdatePasswordDTO.getOldPasswordRequestKey());
        String newPassword = SecurityUtils.decryptGetPassword(sysUpdatePasswordDTO.getNewPassword(), sysUpdatePasswordDTO.getNewPasswordRequestKey());
        String confirmPassword = SecurityUtils.decryptGetPassword(sysUpdatePasswordDTO.getConfirmPassword(), sysUpdatePasswordDTO.getConfirmPasswordRequestKey());

        // 获取旧密码
        String currentPassword = sysProfileService.getPassword();

        if (!SecurityUtils.matchesPassword(oldPassword, currentPassword)) {
            return error(ResultCodeEnum.ERROR,"旧密码输入错误");
        }

        if (SecurityUtils.matchesPassword(newPassword, currentPassword)) {
            return error(ResultCodeEnum.ERROR,"新密码不能与旧密码相同");
        }

        if (!newPassword.equals(confirmPassword)) {
            return error(ResultCodeEnum.ERROR, "两次输入的密码不一致");
        }

        if (newPassword.length() < 6 || newPassword.length() > 22) {
            return error(ResultCodeEnum.ERROR, "密码长度为6-22字符");
        }

        if (isDefaultPassword(newPassword)) {
            return  error(ResultCodeEnum.ERROR,"新密码不能为默认密码");
        }

        return success(sysProfileService.updatePassword(newPassword));
    }

    /**
     * 设置默认部门
     */
    @PostMapping("default/{id}")
    public String setDefaultDept(@PathVariable("id") String id) {
        return success(sysUserDeptService.setDefaultDept(id));
    }

    /**
     * 判断密码是否为默认密码
     */
    private boolean isDefaultPassword(String newPassword) {
        String defaultPassword = sysSettingService.getDefaultPassword();
        // 对比解密后的默认密码
        return SecurityUtils.defaultPasswordDecrypt(defaultPassword).equals(newPassword);
    }
}
