package com.lihua.system.controller;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.lihua.config.LihuaConfig;
import com.lihua.enums.ResultCodeEnum;
import com.lihua.model.web.ControllerResult;
import com.lihua.model.security.SysUserVO;
import com.lihua.system.service.SysAuthenticationService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("system")
public class SysAuthenticationController extends ControllerResult {
    @Resource
    private SysAuthenticationService sysAuthenticationService;

    @Resource
    private CaptchaService captchaService;

    @Resource
    private LihuaConfig lihuaConfig;

    @PostMapping("login")
    public String login(@RequestBody SysUserVO sysUserVO,String captchaVerification) {
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
}
