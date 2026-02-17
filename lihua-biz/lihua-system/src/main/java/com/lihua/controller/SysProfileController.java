package com.lihua.controller;

import com.lihua.annotation.Log;
import com.lihua.entity.SysUser;
import com.lihua.enums.LogTypeEnum;
import com.lihua.model.CurrentDept;
import com.lihua.model.dto.SysUpdatePasswordDTO;
import com.lihua.model.validation.ProfileValidation;
import com.lihua.service.SysProfileService;
import com.lihua.service.SysSettingService;
import com.lihua.service.SysUserDeptService;
import com.lihua.utils.SecurityUtils;
import enums.ResultCodeEnum;
import jakarta.annotation.Resource;
import model.web.ApiResponseModel;
import model.web.basecontroller.ApiResponseController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("system/profile")
public class SysProfileController extends ApiResponseController {

    @Resource
    private SysProfileService sysProfileService;

    @Resource
    private SysUserDeptService sysUserDeptService;

    @Resource
    private SysSettingService sysSettingService;

    @PostMapping("basics")
    @Log(description = "保存个人信息", type = LogTypeEnum.SAVE)
    public ApiResponseModel<String> saveBasics(@RequestBody @Validated(ProfileValidation.ProfileSaveValidation.class) SysUser sysUser) {
        return success(sysProfileService.saveBasics(sysUser));
    }

    @PostMapping("theme")
    public ApiResponseModel<String> saveTheme(@RequestBody @Validated(ProfileValidation.ProfileThemeValidation.class) SysUser sysUser) {
        return success(sysProfileService.saveTheme(sysUser.getTheme()));
    }

    @PostMapping("password")
    @Log(description = "修改密码", type = LogTypeEnum.SAVE, excludeParams = {"oldPassword", "newPassword", "confirmPassword" ,"oldPasswordRequestKey", "newPasswordRequestKey", "confirmPasswordRequestKey"})
    public ApiResponseModel<String> updatePassword(@RequestBody @Validated SysUpdatePasswordDTO sysUpdatePasswordDTO) {
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
            return error(ResultCodeEnum.ERROR,"新密码不能为默认密码");
        }

        return success(sysProfileService.updatePassword(newPassword));
    }

    @PostMapping("default/{id}")
    public ApiResponseModel<CurrentDept> setDefaultDept(@PathVariable("id") String id) {
        return success(sysUserDeptService.setDefaultDept(id));
    }

    /**
     * 判断密码是否为默认密码
     */
    private boolean isDefaultPassword(String newPassword) {
        String defaultPassword = sysSettingService.getDefaultPassword();
        // 对比解密后的默认密码
        return defaultPassword.equals(newPassword);
    }
}
