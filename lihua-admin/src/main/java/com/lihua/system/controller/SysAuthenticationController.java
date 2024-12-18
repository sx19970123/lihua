package com.lihua.system.controller;

import cloud.tianai.captcha.application.ImageCaptchaApplication;
import cloud.tianai.captcha.spring.plugins.secondary.SecondaryVerificationApplication;
import com.lihua.annotation.Log;
import com.lihua.annotation.RateLimiter;
import com.lihua.enums.LogTypeEnum;
import com.lihua.enums.ResultCodeEnum;
import com.lihua.model.security.AuthInfo;
import com.lihua.model.security.CurrentDept;
import com.lihua.model.security.CurrentUser;
import com.lihua.model.security.LoginUser;
import com.lihua.model.web.BaseController;
import com.lihua.system.model.dto.SysRegisterDTO;
import com.lihua.system.service.SysAuthenticationService;
import com.lihua.system.service.SysSettingService;
import com.lihua.utils.security.LoginUserContext;
import com.lihua.utils.security.SecurityUtils;
import com.lihua.utils.tree.TreeUtils;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("system")
public class SysAuthenticationController extends BaseController {

    @Resource
    private SysAuthenticationService sysAuthenticationService;

    @Resource
    private SysSettingService sysSettingService;

    @Resource
    private ImageCaptchaApplication imageCaptchaApplication;

    /**
     * 用户登录
     */
    @PostMapping("login")
    @RateLimiter
    @Log(description = "用户登录", type = LogTypeEnum.LOGIN, excludeParams = {"password", "requestKey"}, recordResult = false)
    public String login(@RequestBody @Valid CurrentUser currentUser) {
        // 校验验证码
        boolean checked = checkCaptcha(currentUser.getCaptchaVerification());
        if (!checked) {
            return error(ResultCodeEnum.CAPTCHA_ERROR);
        }

        // 0.对密码进行AES解密
        currentUser.setPassword(SecurityUtils.decryptGetPassword(currentUser.getPassword(), currentUser.getRequestKey()));
        // 1.用户登录
        LoginUser loginUser = sysAuthenticationService.login(currentUser);
        // 2.生成token
        String token = sysAuthenticationService.cacheAndCreateToken(loginUser);
        // 3.检查是否配置了同账号最大同时登录数，超出数量后首先登录的用户会被踢下线
        sysAuthenticationService.checkSameAccount(token);

        return success(ResultCodeEnum.SUCCESS, token);
    }

    /**
     * 获取登录公钥
     */
    @GetMapping("publicKey/{requestKey}")
    public String getPublicKey(@PathVariable("requestKey") String requestKey) {
        return success(SecurityUtils.getPasswordPublicKey(requestKey));
    }

    /**
     * 检查登录配置
     */
    @GetMapping("checkLoginSetting")
    public String checkLoginSetting() {
        return success(sysAuthenticationService.checkLoginSetting(LoginUserContext.getLoginUser()));
    }

    /**
     * 从 SecurityContextHolder 中获取用户信息返回
     */
    @GetMapping("info")
    public String getUserInfo() {
        LoginUser loginUser = LoginUserContext.getLoginUser();
        // 前端 store 用户数据
        AuthInfo authInfo = new AuthInfo();
        authInfo.setUserInfo(loginUser.getUser() != null ? loginUser.getUser() : new CurrentUser());
        authInfo.setDepts(TreeUtils.buildTree(loginUser.getDeptList()));
        authInfo.setPosts(loginUser.getPostList());
        authInfo.setRoles(loginUser.getRoleList());
        authInfo.setPermissions(loginUser.getPermissionList().stream().filter(item -> !item.startsWith("ROLE_")).toList());
        authInfo.setRouters(loginUser.getRouterList());
        authInfo.setViewTabs(loginUser.getViewTabList());
        authInfo.setDefaultDept(LoginUserContext.getDefaultDept() != null ? LoginUserContext.getDefaultDept() : new CurrentDept());
        return success(authInfo);
    }

    /**
     * 数据更新
     */
    @PostMapping("reloadData")
    @RateLimiter
    public String reloadData() {
        sysAuthenticationService.cacheLoginUserInfo(LoginUserContext.getLoginUser());
        return success();
    }

    /**
     * 检查用户名是否重复
     */
    @PostMapping("checkUserName/{username}")
    public String checkUserName(@PathVariable("username") String username) {
        return success(sysAuthenticationService.checkUserName(username));
    }

    @PostMapping("register")
    @RateLimiter
    @Log(description = "用户注册", type = LogTypeEnum.REGISTER, excludeParams = {"password", "confirmPassword"}, recordResult = false)
    public String register(@RequestBody @Valid SysRegisterDTO sysRegisterDTO) {
        // 校验验证码
        boolean checked = checkCaptcha(sysRegisterDTO.getCaptchaVerification());
        if (!checked) {
            return error(ResultCodeEnum.CAPTCHA_ERROR);
        }

        // 获取解密后的密码
        String password = SecurityUtils.decryptGetPassword(sysRegisterDTO.getPassword(), sysRegisterDTO.getPasswordRequestKey());

        // 密码长度校验
        if (password.length() < 6 || password.length() >= 30 ) {
            return error(ResultCodeEnum.ERROR, "密码长度6-30位");
        }

        // 获取解密后的确认密码
        String confirmPassword = SecurityUtils.decryptGetPassword(sysRegisterDTO.getConfirmPassword(), sysRegisterDTO.getConfirmPasswordRequestKey());

        // 校验两次密码输入是否相同
        if (!password.equals(confirmPassword)) {
            return error(ResultCodeEnum.ERROR, "两次输入的密码不一致");
        }

        // 注册
        return success(sysAuthenticationService.register(sysRegisterDTO.getUsername(), password));
    }

    // 校验验证码
    private boolean checkCaptcha(String captchaVerification) {
        if (!sysSettingService.enableCaptcha()) {
            return true;
        }

        if (imageCaptchaApplication instanceof SecondaryVerificationApplication) {
            return ((SecondaryVerificationApplication) imageCaptchaApplication).secondaryVerification(captchaVerification);
        }
        return false;
    }
}
