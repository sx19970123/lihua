package com.lihua.system.controller;

import com.lihua.enums.ResultCodeEnum;
import com.lihua.model.security.AuthInfo;
import com.lihua.model.security.CurrentDept;
import com.lihua.model.security.CurrentUser;
import com.lihua.model.security.LoginUser;
import com.lihua.model.web.BaseController;
import com.lihua.system.entity.SysUser;
import com.lihua.system.entity.validation.ProfileValidation;
import com.lihua.system.service.SysProfileService;
import com.lihua.utils.security.LoginUserContext;
import com.lihua.utils.security.SecurityUtils;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("system/profile")
public class SysProfileController extends BaseController {

    @Resource
    private SysProfileService sysProfileService;

    /**
     * 从 SecurityContextHolder 中获取用户信息返回
     * @return
     */
    @GetMapping("info")
    public String getUserInfo() {
        LoginUser loginUser = LoginUserContext.getLoginUser();
        loginUser.getUser().setPassword(null);

        // 前端 store 用户数据
        AuthInfo authInfo = new AuthInfo();
        authInfo.setUserInfo(loginUser.getUser() != null ? loginUser.getUser() : new CurrentUser());
        authInfo.setDepts(loginUser.getDeptTree());
        authInfo.setPosts(loginUser.getPostList());
        authInfo.setRoles(loginUser.getRoleList());
        authInfo.setPermissions(loginUser.getPermissionList().stream().map(GrantedAuthority::getAuthority).toList());
        authInfo.setRouters(loginUser.getRouterList());
        authInfo.setViewTabs(loginUser.getViewTabList());
        authInfo.setDefaultDept(LoginUserContext.getDefaultDept() != null ? LoginUserContext.getDefaultDept() : new CurrentDept());
        return success(authInfo);
    }

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
}
