package com.anji.captcha.controller;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.anji.captcha.util.StringUtils;
import com.lihua.model.web.BaseController;
import com.lihua.system.entity.SysSetting;
import com.lihua.system.service.SysSettingService;
import com.lihua.utils.json.JsonUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


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
     * @return
     */
    @GetMapping("enable")
    public String enable() {
        SysSetting verificationCodeSetting = sysSettingService.getSysSettingByComponentName("VerificationCodeSetting");

        if (verificationCodeSetting == null) {
            return success(true);
        }

        // 获取具体配置后解析json返回是否启用验证码
        // 出现任何值为空都认为需要验证码
        String settingJson = verificationCodeSetting.getSettingJson();

        try {
            if (org.springframework.util.StringUtils.hasText(settingJson)) {
                HashMap<String, Boolean> map = JsonUtils.toObject(settingJson, HashMap.class);

                if (map == null) {
                    return success(true);
                }

                Boolean enable = map.get("enable");

                if (enable == null) {
                    return success(true);
                }

                return success(enable);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return success(true);
        }

        return success(true);
    }

    /**
     * 获取验证码
     * @param data
     * @param request
     * @return
     */
    @PostMapping({"get"})
    public ResponseModel get(@RequestBody CaptchaVO data, HttpServletRequest request) {
        assert request.getRemoteHost() != null;

        data.setBrowserInfo(getRemoteId(request));
        return this.captchaService.get(data);
    }

    /**
     * 校验验证码
     * @param data
     * @param request
     * @return
     */
    @PostMapping({"check"})
    public ResponseModel check(@RequestBody CaptchaVO data, HttpServletRequest request) {
        data.setBrowserInfo(getRemoteId(request));
        return this.captchaService.check(data);
    }

    public ResponseModel verify(@RequestBody CaptchaVO data, HttpServletRequest request) {
        return this.captchaService.verification(data);
    }

    public static final String getRemoteId(HttpServletRequest request) {
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