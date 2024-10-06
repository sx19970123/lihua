package com.lihua.system.controller;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
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
import com.lihua.system.service.SysProfileService;
import com.lihua.system.service.SysSettingService;
import com.lihua.utils.security.LoginUserContext;
import com.lihua.utils.security.SecurityUtils;
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
    private SysSettingService sysSettingService;

    @Resource
    private SysProfileService sysProfileService;

    /**
     * 用户登录
     * @param currentUser
     */
    @PostMapping("login")
    @RateLimiter
    @Log(description = "用户登录", type = LogTypeEnum.LOGIN, excludeParams = {"password"}, recordResult = false)
    public String login(@RequestBody @Valid CurrentUser currentUser) {
        // 开启验证码情况下进行验证
        if (sysSettingService.enableCaptcha()) {
            CaptchaVO captchaVO = new CaptchaVO();
            captchaVO.setCaptchaVerification(currentUser.getCaptchaVerification());
            ResponseModel verificationModel = captchaService.verification(captchaVO);
            if (!verificationModel.isSuccess()) {
                return error(ResultCodeEnum.ERROR, verificationModel.getRepMsg());
            }
        }

        // 对密码进行AES解密
        try {
            currentUser.setPassword(SecurityUtils.decryptGetPassword(currentUser.getPassword(), currentUser.getRequestKey()));
        } catch (Exception e) {
            return error(ResultCodeEnum.ERROR, "登录失败");
        }

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
     * @param requestKey
     * @return
     */
    @GetMapping("publicKey/{requestKey}")
    public String getPublicKey(@PathVariable("requestKey") String requestKey) {
        return success(SecurityUtils.getPasswordPublicKey(requestKey));
    }

    /**
     * 检擦登录配置
     * @return
     */
    @GetMapping("checkLoginSetting")
    public String checkLoginSetting() {
        String loginSettingComponentName = sysAuthenticationService
                .checkLoginSetting(LoginUserContext.getLoginUser(), sysProfileService.getPassword());
        return success(loginSettingComponentName);
    }

    /**
     * 从 SecurityContextHolder 中获取用户信息返回
     * @return
     */
    @GetMapping("info")
    public String getUserInfo() {
        LoginUser loginUser = LoginUserContext.getLoginUser();
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
    @RateLimiter
    public String reloadData() {
        sysAuthenticationService.cacheLoginUserInfo(LoginUserContext.getLoginUser());
        return success();
    }

    /**
     * 检查用户名是否重复
     * @param username
     * @return
     */
    @PostMapping("checkUserName/{username}")
    public String checkUserName(@PathVariable("username") String username) {
        return success(sysAuthenticationService.checkUserName(username));
    }

    @PostMapping("register")
    @RateLimiter
    @Log(description = "用户注册", type = LogTypeEnum.REGISTER, excludeParams = {"password", "confirmPassword"}, recordResult = false)
    public String register(@RequestBody @Valid SysRegisterDTO sysRegisterDTO, String captchaVerification) {
        // 校验两次密码输入是否相同
        if (!sysRegisterDTO.getPassword().equals(sysRegisterDTO.getConfirmPassword())) {
            return error(ResultCodeEnum.ERROR, "两次输入的密码不一致");
        }
        // 校验验证码
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaVerification(captchaVerification);
        ResponseModel verificationModel = captchaService.verification(captchaVO);
        if (!verificationModel.isSuccess()) {
            return error(ResultCodeEnum.ERROR, verificationModel.getRepMsg());
        }

        // 注册
        return success(sysAuthenticationService.register(sysRegisterDTO.getUsername(), sysRegisterDTO.getPassword()));
    }
}
