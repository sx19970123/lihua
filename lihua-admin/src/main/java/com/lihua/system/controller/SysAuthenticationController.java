package com.lihua.system.controller;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
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

    @PostMapping("login")
    public String login(@RequestBody SysUserVO sysUserVO,String captchaVerification) {
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaVerification(captchaVerification);
        ResponseModel verification = captchaService.verification(captchaVO);

        if (verification.isSuccess()) {
            return success(sysAuthenticationService.login(sysUserVO));
        }

        return error(ResultCodeEnum.ERROR,verification.getRepMsg());
    }
}
