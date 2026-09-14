package com.lihua.controller.app;

import com.lihua.common.model.response.ApiResponseModel;
import com.lihua.common.utils.tree.TreeUtils;
import com.lihua.controller.base.BaseSysProfileController;
import com.lihua.log.annotation.Log;
import com.lihua.log.enums.LogTypeEnum;
import com.lihua.model.dto.SysCheckPasswordDTO;
import com.lihua.model.dto.SysUpdatePasswordDTO;
import com.lihua.security.manager.LoginUserContext;
import com.lihua.security.model.AuthInfo;
import com.lihua.security.model.CurrentDept;
import com.lihua.security.model.CurrentUser;
import com.lihua.security.model.LoginUserSession;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * App 版个人中心：基本信息保存、修改密码与默认部门由基类提供；
 * 用户信息不含路由与视图标签（App 无 Web 菜单体系），注销与密码验证仅 App 版提供
 */
@Tag(name = "APP-个人中心")
@RestController
@RequestMapping("app/system/profile")
public class AppSysProfileController extends BaseSysProfileController {

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
