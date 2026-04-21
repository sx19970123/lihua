package com.lihua.controller;

import cloud.tianai.captcha.application.ImageCaptchaApplication;
import cloud.tianai.captcha.spring.plugins.secondary.SecondaryVerificationApplication;
import com.lihua.common.enums.ResultCodeEnum;
import com.lihua.common.model.response.ApiResponseModel;
import com.lihua.common.model.response.basecontroller.ApiResponseController;
import com.lihua.log.annotation.Log;
import com.lihua.log.enums.LogTypeEnum;
import com.lihua.model.dto.SysLoginUserDTO;
import com.lihua.model.dto.SysRegisterDTO;
import com.lihua.security.manager.LoginUserContext;
import com.lihua.security.manager.LoginUserManager;
import com.lihua.security.model.LoginUserSession;
import com.lihua.service.SysAuthenticationService;
import com.lihua.service.SysSettingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 用户身份验证/授权/登录数据获取/注册
 */
@Tag(name = "身份验证、注册接口")
@RestController
@RequestMapping("system")
public class SysAuthenticationController extends ApiResponseController {

    @Resource
    private SysAuthenticationService sysAuthenticationService;

    @Resource
    private SysSettingService sysSettingService;

    @Resource
    private ImageCaptchaApplication imageCaptchaApplication;

    /**
     * 用户登录
     */
    @Operation(summary = "用户登录")
    @PostMapping("login")
    @Log(description = "用户登录", type = LogTypeEnum.LOGIN, excludeParams = {"password"}, recordResult = false)
    public ApiResponseModel<String> login(@RequestBody @Valid SysLoginUserDTO loginUserDTO) {
        // 校验验证码
        boolean checked = checkCaptcha(loginUserDTO.getCaptchaVerification());
        if (!checked) {
            return error(ResultCodeEnum.CAPTCHA_ERROR);
        }

        // 1.用户登录
        LoginUserSession loginUserSession = sysAuthenticationService.login(loginUserDTO);
        // 2.生成token
        String token = sysAuthenticationService.cacheAndCreateToken(loginUserSession);
        // 3.检查是否配置了同账号最大同时登录数，超出数量后首先登录的用户会被踢下线
        sysAuthenticationService.checkSameAccount(token);

        return success(ResultCodeEnum.SUCCESS, token);
    }

    /**
     * 数据更新
     */
    @Operation(summary = "重新加载当前登录用户信息")
    @PostMapping("reloadData")
    public ApiResponseModel<String> reloadData() {
        sysAuthenticationService.cacheLoginUserInfo(LoginUserContext.getLoginUser());
        LoginUserManager.refreshToken();
        return success();
    }

    /**
     * 检查用户名是否重复
     */
    @Operation(summary = "检查用户名是否重复")
    @PostMapping("checkUserName/{username}")
    public ApiResponseModel<Boolean> checkUserName(@PathVariable("username") String username) {
        return success(sysAuthenticationService.checkUserName(username));
    }

    /**
     * 用户注册
     */
    @Operation(summary = "用户注册")
    @PostMapping("register")
    @Log(description = "用户注册", type = LogTypeEnum.REGISTER, excludeParams = {"password", "confirmPassword"}, recordResult = false)
    public ApiResponseModel<String> register(@RequestBody @Valid SysRegisterDTO sysRegisterDTO) {
        // 校验验证码
        boolean checked = checkCaptcha(sysRegisterDTO.getCaptchaVerification());
        if (!checked) {
            return error(ResultCodeEnum.CAPTCHA_ERROR);
        }

        // 获取解密后的密码
        String password = sysRegisterDTO.getPassword();

        // 密码长度校验
        if (password.length() < 6 || password.length() >= 30 ) {
            return error(ResultCodeEnum.ERROR, "密码长度6-30位");
        }

        // 获取解密后的确认密码
        String confirmPassword = sysRegisterDTO.getConfirmPassword();

        // 校验两次密码输入是否相同
        if (!password.equals(confirmPassword)) {
            return error(ResultCodeEnum.ERROR, "两次输入的密码不一致");
        }

        // 注册
        return success(sysAuthenticationService.register(sysRegisterDTO.getUsername(), password));
    }

    @Operation(summary = "获取一次性令牌")
    @GetMapping("onceToken")
    public ApiResponseModel<String> getOnceToken() {
        return success(sysAuthenticationService.getOnceToken());
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
