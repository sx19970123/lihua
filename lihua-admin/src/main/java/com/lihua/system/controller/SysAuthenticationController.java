package com.lihua.system.controller;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.lihua.annotation.Log;
import com.lihua.config.LihuaConfig;
import com.lihua.enums.LogTypeEnum;
import com.lihua.enums.ResultCodeEnum;
import com.lihua.model.security.AuthInfo;
import com.lihua.model.security.CurrentDept;
import com.lihua.model.security.CurrentUser;
import com.lihua.model.security.LoginUser;
import com.lihua.model.web.BaseController;
import com.lihua.system.service.SysAuthenticationService;
import com.lihua.utils.security.LoginUserContext;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("system")
public class SysAuthenticationController extends BaseController {
    @Resource
    private SysAuthenticationService sysAuthenticationService;

    @Resource
    private CaptchaService captchaService;

    @Resource
    private LihuaConfig lihuaConfig;

    /**
     * 用户登录
     * @param currentUser
     * @param captchaVerification
     * @return
     */
    @PostMapping("login")
    @Log(description = "用户登录", type = LogTypeEnum.LOGIN, excludeParams = {"password", "username"})
    public String login(@RequestBody @Valid CurrentUser currentUser, String captchaVerification) {
        // 开启验证码情况下进行验证
        if (lihuaConfig.getEnableVerificationCode() != null && lihuaConfig.getEnableVerificationCode()) {
            CaptchaVO captchaVO = new CaptchaVO();
            captchaVO.setCaptchaVerification(captchaVerification);
            ResponseModel verificationModel = captchaService.verification(captchaVO);
            if (!verificationModel.isSuccess()) {
                return error(ResultCodeEnum.ERROR, verificationModel.getRepMsg());
            }
        }
        return success(sysAuthenticationService.login(currentUser));
    }

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
        authInfo.setPermissions(loginUser.getPermissionList().stream().map(GrantedAuthority::getAuthority).filter(item -> !item.startsWith("ROLE_")).toList());
        authInfo.setRouters(loginUser.getRouterList());
        authInfo.setViewTabs(loginUser.getViewTabList());
        authInfo.setDefaultDept(LoginUserContext.getDefaultDept() != null ? LoginUserContext.getDefaultDept() : new CurrentDept());
        return success(authInfo);
    }

    /**
     * 数据更新
     * @return
     */
    @PostMapping("reloadData")
    public String reloadData() {
        sysAuthenticationService.cacheUserLoginDetails(LoginUserContext.getLoginUser());
        return success();
    }
}
