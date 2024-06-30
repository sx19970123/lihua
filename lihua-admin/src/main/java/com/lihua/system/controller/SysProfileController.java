package com.lihua.system.controller;

import com.lihua.config.LihuaConfig;
import com.lihua.enums.ResultCodeEnum;
import com.lihua.model.security.AuthInfo;
import com.lihua.model.security.CurrentDept;
import com.lihua.model.security.CurrentUser;
import com.lihua.model.security.LoginUser;
import com.lihua.model.web.BaseController;
import com.lihua.system.entity.SysUser;
import com.lihua.system.entity.validation.ProfileValidation;
import com.lihua.system.service.SysProfileService;
import com.lihua.system.service.SysUserDeptService;
import com.lihua.utils.file.FileDownloadUtils;
import com.lihua.utils.security.LoginUserContext;
import com.lihua.utils.security.SecurityUtils;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
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

    /**
     * 保存基础信息
     * @param sysUser
     * @return
     */
    @PostMapping("basics")
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
    public String updatePassword(@NotNull(message = "旧密码不能为空") String oldPassword,
                                 @NotNull(message = "新密码不能为空") @Size(min = 6, max = 22, message = "密码长度为6-22字符") String newPassword) {
        if (!SecurityUtils.matchesPassword(oldPassword,LoginUserContext.getUser().getPassword())) {
            return error(ResultCodeEnum.ERROR,"旧密码输入错误");
        }
        if (SecurityUtils.matchesPassword(newPassword,LoginUserContext.getUser().getPassword())) {
            return error(ResultCodeEnum.ERROR,"新密码不能与旧密码相同");
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

}
