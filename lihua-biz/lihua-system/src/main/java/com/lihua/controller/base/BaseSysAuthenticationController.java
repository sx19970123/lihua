package com.lihua.controller.base;

import cloud.tianai.captcha.application.ImageCaptchaApplication;
import cloud.tianai.captcha.spring.plugins.secondary.SecondaryVerificationApplication;
import com.lihua.common.enums.ResultCodeEnum;
import com.lihua.common.model.response.ApiResponseModel;
import com.lihua.common.model.response.basecontroller.ApiResponseController;
import com.lihua.log.annotation.Log;
import com.lihua.log.enums.LogTypeEnum;
import com.lihua.model.dto.SysLoginUserDTO;
import com.lihua.model.dto.SysRegisterDTO;
import com.lihua.security.model.LoginUserSession;
import com.lihua.service.SysAuthenticationService;
import com.lihua.service.SysSettingService;
import com.lihua.web.annotation.PreventDuplicateSubmit;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 身份验证控制器基类：承载管理版与 App 版共有端点（对外行为双版本一致，差异仅在路由前缀与文档分组）；
 * 登录数据重载存在端侧差异（Web 版刷新 token 有效期），由各子类自行声明
 */
public abstract class BaseSysAuthenticationController extends ApiResponseController {

    @Resource
    protected SysAuthenticationService sysAuthenticationService;

    @Resource
    protected SysSettingService sysSettingService;

    @Resource
    protected ImageCaptchaApplication imageCaptchaApplication;

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
     * 用户注册
     */
    @Operation(summary = "用户注册")
    @PostMapping("register")
    @PreventDuplicateSubmit
    @Log(description = "用户注册", type = LogTypeEnum.REGISTER, excludeParams = {"password", "confirmPassword"}, recordResult = false)
    public ApiResponseModel<String> register(@RequestBody @Valid SysRegisterDTO sysRegisterDTO) {
        // 校验验证码
        boolean checked = checkCaptcha(sysRegisterDTO.getCaptchaVerification());
        if (!checked) {
            return error(ResultCodeEnum.CAPTCHA_ERROR);
        }

        String password = sysRegisterDTO.getPassword();
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
