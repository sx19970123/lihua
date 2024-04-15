package com.lihua.system.controller;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.lihua.config.LihuaConfig;
import com.lihua.enums.ResultCodeEnum;
import com.lihua.model.web.BaseController;
import com.lihua.model.security.SysUserVO;
import com.lihua.system.service.SysAuthenticationService;
import com.lihua.utils.security.LoginUserContext;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("system")
@Validated
public class SysAuthenticationController extends BaseController {
    @Resource
    private SysAuthenticationService sysAuthenticationService;

    @Resource
    private CaptchaService captchaService;

    @Resource
    private LihuaConfig lihuaConfig;

    /**
     * 用户登录
     * @param sysUserVO
     * @param captchaVerification
     * @return
     */
    @PostMapping("login")
    public String login(@RequestBody SysUserVO sysUserVO, String captchaVerification) {
        // 开启验证码情况下进行验证
        if (lihuaConfig.getEnableVerificationCode() != null && lihuaConfig.getEnableVerificationCode()) {
            CaptchaVO captchaVO = new CaptchaVO();
            captchaVO.setCaptchaVerification(captchaVerification);
            ResponseModel verificationModel = captchaService.verification(captchaVO);
            if (!verificationModel.isSuccess()) {
                return error(ResultCodeEnum.ERROR, verificationModel.getRepMsg());
            }
        }
        return success(sysAuthenticationService.login(sysUserVO));
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
