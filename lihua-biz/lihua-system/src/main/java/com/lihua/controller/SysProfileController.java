package com.lihua.controller;

import com.lihua.controller.base.BaseSysProfileController;
import com.lihua.common.model.response.ApiResponseModel;
import com.lihua.common.utils.tree.TreeUtils;
import com.lihua.log.annotation.Log;
import com.lihua.log.enums.LogTypeEnum;
import com.lihua.model.dto.SysProfileBasicDTO;
import com.lihua.model.validation.ProfileValidation;
import com.lihua.security.manager.LoginUserContext;
import com.lihua.security.model.AuthInfo;
import com.lihua.security.model.CurrentDept;
import com.lihua.security.model.CurrentUser;
import com.lihua.security.model.LoginUserSession;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "个人中心")
@RestController
@RequestMapping("system/profile")
public class SysProfileController extends BaseSysProfileController {

    @Operation(summary = "保存主题")
    @PostMapping("theme")
    @Log(description = "保存主题", type = LogTypeEnum.SAVE)
    public ApiResponseModel<String> saveTheme(@RequestBody @Validated(ProfileValidation.ProfileThemeValidation.class) SysProfileBasicDTO sysProfileBasicDTO) {
        return success(sysProfileService.saveTheme(sysProfileBasicDTO.getTheme()));
    }

    @Operation(summary = "登录后用户数据校验")
    @GetMapping("postLoginCheck")
    public ApiResponseModel<List<String>> postLoginCheck() {
        return success(sysProfileService.postLoginCheck());
    }

    /**
     * 从 SecurityContextHolder 中获取用户信息返回
     */
    @Operation(summary = "获取当前登录用户信息")
    @GetMapping("info")
    public ApiResponseModel<AuthInfo> getUserInfo() {
        LoginUserSession loginUserSession = LoginUserContext.getLoginUser();
        // 前端 store 用户数据
        AuthInfo authInfo = new AuthInfo();
        CurrentUser userInfo = loginUserSession.getUser() != null ? loginUserSession.getUser() : new CurrentUser();
        // 头像 JSON 下发前转换：image 型 value 由对象键转为可直接访问的相对链（单字段契约，前端不再拼接 URL）
        userInfo.setAvatar(sysUserService.processAvatarUrl(userInfo.getAvatar()));
        authInfo.setUserInfo(userInfo);
        authInfo.setDepts(TreeUtils.buildTree(loginUserSession.getDeptList()));
        authInfo.setPosts(loginUserSession.getPostList());
        authInfo.setRoles(loginUserSession.getRoleList());
        authInfo.setPermissions(loginUserSession.getPermissionList().stream().filter(item -> !item.startsWith("ROLE_")).toList());
        authInfo.setRouters(loginUserSession.getRouterList());
        authInfo.setViewTabs(loginUserSession.getViewTabList());
        authInfo.setDefaultDept(LoginUserContext.getDefaultDept() != null ? LoginUserContext.getDefaultDept() : new CurrentDept());
        return success(authInfo);
    }
}
