package com.lihua.system.controller;

import cloud.tianai.captcha.application.ImageCaptchaApplication;
import cloud.tianai.captcha.application.vo.CaptchaResponse;
import cloud.tianai.captcha.application.vo.ImageCaptchaVO;
import cloud.tianai.captcha.common.constant.CaptchaTypeConstant;
import cloud.tianai.captcha.common.response.ApiResponse;
import cloud.tianai.captcha.validator.common.model.dto.ImageCaptchaTrack;
import com.lihua.annotation.PreventDuplicateSubmit;
import com.lihua.model.web.BaseController;
import com.lihua.system.service.SysSettingService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;


@RestController
@RequestMapping("captcha")
public class CaptchaController extends BaseController {

    @Resource
    private ImageCaptchaApplication imageCaptchaApplication;

    @Resource
    private SysSettingService sysSettingService;

    // 验证码验证方式枚举
    private static final String[] CAPTCHA_TYPES = {
            CaptchaTypeConstant.SLIDER,
            CaptchaTypeConstant.CONCAT,
            CaptchaTypeConstant.ROTATE,
            CaptchaTypeConstant.WORD_IMAGE_CLICK
    };

    // 是否启用验证码
    @GetMapping("enable")
    public String enable() {
        return success(sysSettingService.enableCaptcha());
    }

    // 获取验证码
    @PostMapping("get")
    public CaptchaResponse<ImageCaptchaVO> getCaptcha() {
        // 获取随机验证码类型
        String type = CAPTCHA_TYPES[ThreadLocalRandom.current().nextInt(CAPTCHA_TYPES.length)];
        return imageCaptchaApplication.generateCaptcha(type);
    }

    // 校验验证码
    @PostMapping("check")
    public ApiResponse<?> checkCaptcha(@RequestBody Data data) {
        ApiResponse<?> response = imageCaptchaApplication.matching(data.getId(), data.getData());
        if (response.isSuccess()) {
            return ApiResponse.ofSuccess(Collections.singletonMap("id", data.getId()));
        }
        return response;
    }

    // 验证码接收内部类
    @lombok.Data
    public static class Data {
        private String  id;
        private ImageCaptchaTrack data;
    }

}
