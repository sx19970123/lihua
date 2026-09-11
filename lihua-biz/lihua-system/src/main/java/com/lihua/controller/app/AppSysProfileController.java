package com.lihua.controller.app;

import com.lihua.common.model.response.ApiResponseModel;
import com.lihua.common.model.response.basecontroller.ApiResponseController;
import com.lihua.common.utils.tree.TreeUtils;
import com.lihua.security.manager.LoginUserContext;
import com.lihua.security.model.AuthInfo;
import com.lihua.security.model.CurrentUser;
import com.lihua.security.model.LoginUserSession;
import com.lihua.log.annotation.Log;
import com.lihua.log.enums.LogTypeEnum;
import com.lihua.model.dto.SysCheckPasswordDTO;
import com.lihua.model.dto.SysProfileBasicDTO;
import com.lihua.model.dto.SysUpdatePasswordDTO;
import com.lihua.model.validation.ProfileValidation;
import com.lihua.security.model.CurrentDept;
import com.lihua.service.SysProfileService;
import com.lihua.service.SysUserDeptService;
import com.lihua.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "APP-个人中心")
@RestController
@RequestMapping("app/system/profile")
public class AppSysProfileController extends ApiResponseController {

    @Resource
    private SysProfileService sysProfileService;

    @Resource
    private SysUserDeptService sysUserDeptService;

    @Resource
    private SysUserService sysUserService;

    @Operation(summary = "保存个人信息")
    @PostMapping("basics")
    @Log(description = "保存个人信息", type = LogTypeEnum.SAVE)
    public ApiResponseModel<String> saveBasics(@RequestBody @Validated(ProfileValidation.ProfileSaveValidation.class) SysProfileBasicDTO sysProfileBasicDTO) {
        return success(sysProfileService.saveBasics(sysProfileBasicDTO));
    }

    @Operation(summary = "修改密码")
    @PostMapping("password")
    @Log(description = "修改密码", type = LogTypeEnum.SAVE, excludeParams = {"oldPassword", "newPassword", "confirmPassword"})
    public ApiResponseModel<String> updatePassword(@RequestBody @Validated SysUpdatePasswordDTO sysUpdatePasswordDTO) {
        return success(sysProfileService.updatePassword(sysUpdatePasswordDTO));
    }

    @Operation(summary = "设置默认部门")
    @PostMapping("default/{id}")
    public ApiResponseModel<CurrentDept> setDefaultDept(@PathVariable("id") String id) {
        return success(sysUserDeptService.setDefaultDept(id));
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
        // 头像可直接访问 URL（非图片类型为 null；avatar 原值 JSON 串仍透传），直接填充在用户信息对象上随 userInfo 下发
        userInfo.setAvatarUrl(sysUserService.resolveAvatarUrl(userInfo.getAvatar()));
        authInfo.setUserInfo(userInfo);
        authInfo.setDepts(TreeUtils.buildTree(loginUserSession.getDeptList()));
        authInfo.setPosts(loginUserSession.getPostList());
        authInfo.setRoles(loginUserSession.getRoleList());
        authInfo.setPermissions(loginUserSession.getPermissionList().stream().filter(item -> !item.startsWith("ROLE_")).toList());
        authInfo.setDefaultDept(LoginUserContext.getDefaultDept() != null ? LoginUserContext.getDefaultDept() : new CurrentDept());
        return success(authInfo);
    }

    @Operation(summary = "用户注销")
    @Log(description = "用户注销", type = LogTypeEnum.DELETE)
    @DeleteMapping("deactivate")
    public ApiResponseModel<String> accountDeactivate() {
        sysProfileService.accountDeactivate();
        return success();
    }

    @Operation(summary = "验证密码")
    @PostMapping("checkPassword")
    @Log(description = "验证密码", type = LogTypeEnum.OTHER, excludeParams = {"password"})
    public ApiResponseModel<Boolean> checkPassword(@RequestBody @Validated SysCheckPasswordDTO sysCheckPasswordDTO) {
        return success(sysProfileService.checkPassword(sysCheckPasswordDTO));
    }
}
