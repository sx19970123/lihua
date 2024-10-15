package com.anji.captcha.controller;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.anji.captcha.util.StringUtils;
import com.lihua.model.web.BaseController;
import com.lihua.system.service.SysSettingService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


/**
 * 验证码请求controller，ajCaptcha 兼容 SpringBoot3 进行controller重写
 */
@RestController
@RequestMapping({"/captcha"})
@Slf4j
public class CaptchaController extends BaseController{

    @Resource
    private CaptchaService captchaService;

    @Resource
    private SysSettingService sysSettingService;
    /**
     * 是否启用验证码
     */
    @GetMapping("enable")
    public String enable() {
        return success(sysSettingService.enableCaptcha());
    }

    /**
     * 获取验证码
     */
    @PostMapping({"get"})
    public ResponseModel get(@RequestBody CaptchaVO data, HttpServletRequest request) {
        assert request.getRemoteHost() != null;

        data.setBrowserInfo(getRemoteId(request));
        return this.captchaService.get(data);
    }

    /**
     * 校验验证码
     */
    @PostMapping({"check"})
    public ResponseModel check(@RequestBody CaptchaVO data, HttpServletRequest request) {
        data.setBrowserInfo(getRemoteId(request));
        return this.captchaService.check(data);
    }

    public ResponseModel verify(@RequestBody CaptchaVO data, HttpServletRequest request) {
        return this.captchaService.verification(data);
    }

    public static String getRemoteId(HttpServletRequest request) {
        String xfwd = request.getHeader("X-Forwarded-For");
        String ip = getRemoteIpFromXfwd(xfwd);
        String ua = request.getHeader("user-agent");
        return StringUtils.isNotBlank(ip) ? ip + ua : request.getRemoteAddr() + ua;
    }

    private static String getRemoteIpFromXfwd(String xfwd) {
        if (StringUtils.isNotBlank(xfwd)) {
            String[] ipList = xfwd.split(",");
            return StringUtils.trim(ipList[0]);
        } else {
            return null;
        }
    }
}