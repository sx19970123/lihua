package com.lihua.system.controller;

import com.lihua.annotation.Log;
import com.lihua.enums.LogTypeEnum;
import com.lihua.enums.ResultCodeEnum;
import com.lihua.exception.ServiceException;
import com.lihua.model.web.BaseController;
import com.lihua.system.entity.SysSetting;
import com.lihua.system.entity.SysUser;
import com.lihua.system.model.dto.SysSettingDTO;
import com.lihua.system.model.validation.ProfileValidation;
import com.lihua.system.service.SysProfileService;
import com.lihua.system.service.SysSettingService;
import com.lihua.system.service.SysUserDeptService;
import com.lihua.utils.json.JsonUtils;
import com.lihua.utils.security.LoginUserContext;
import com.lihua.utils.security.SecurityUtils;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

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
     * @param sysUser
     * @return
     */
    @PostMapping("basics")
    @Log(description = "保存个人信息", type = LogTypeEnum.SAVE)
    public String saveBasics(@RequestBody @Validated(ProfileValidation.ProfileSaveValidation.class) SysUser sysUser) {
        return success(sysProfileService.saveBasics(sysUser));
    }

    /**
     * 保存主题
     * @param sysUser
     * @return
     */
    @PostMapping("theme")
    public String saveTheme(@RequestBody @Validated(ProfileValidation.ProfileThemeValidation.class) SysUser sysUser) {
        return success(sysProfileService.saveTheme(sysUser.getTheme()));
    }

    /**
     * 获取图片用户头像
     * @param fullAvatarPath
     * @return
     */
    @GetMapping("avatar")
    public ResponseEntity<StreamingResponseBody> getAvatar(@RequestParam("fullAvatarPath") String fullAvatarPath) {
        return success(new File(fullAvatarPath));
    }

    /**
     * 修改密码
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @PostMapping("password")
    @Log(description = "修改密码", type = LogTypeEnum.SAVE, excludeParams = {"oldPassword", "newPassword"})
    public String updatePassword(@NotNull(message = "旧密码不能为空") String oldPassword,
                                 @NotNull(message = "新密码不能为空") @Size(min = 6, max = 22, message = "密码长度为6-22字符") String newPassword) {
        String currentPassword  = LoginUserContext.getUser().getPassword();

        if (!SecurityUtils.matchesPassword(oldPassword, currentPassword)) {
            return error(ResultCodeEnum.ERROR,"旧密码输入错误");
        }

        if (SecurityUtils.matchesPassword(newPassword, currentPassword)) {
            return error(ResultCodeEnum.ERROR,"新密码不能与旧密码相同");
        }

        if (isDefaultPassword(newPassword)) {
            return  error(ResultCodeEnum.ERROR,"新密码不能为默认密码");
        }

        return success(sysProfileService.updatePassword(newPassword));
    }

    /**
     * 设置默认部门
     * @param id
     * @return
     */
    @PostMapping("default/{id}")
    public String setDefaultDept(@PathVariable("id") String id) {
        return success(sysUserDeptService.setDefaultDept(id));
    }

    /**
     * 判断密码是否为默认密码
     * @param newPassword
     * @return
     */
    private boolean isDefaultPassword(String newPassword) {
        SysSetting setting = sysSettingService.getSysSettingByComponentName("DefaultPasswordSetting");

        if (setting == null) {
            return false;
        }

        String settingJson = setting.getSettingJson();
        SysSettingDTO.DefaultPasswordSetting defaultPasswordSetting = JsonUtils.toObject(settingJson, SysSettingDTO.DefaultPasswordSetting.class);
        String defaultPassword = defaultPasswordSetting.getDefaultPassword();
        return defaultPassword != null && defaultPassword.equals(newPassword);
    }
}
