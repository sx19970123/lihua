package com.lihua.controller.system;

import cloud.tianai.captcha.application.ImageCaptchaApplication;
import cloud.tianai.captcha.application.vo.CaptchaResponse;
import cloud.tianai.captcha.application.vo.ImageCaptchaVO;
import cloud.tianai.captcha.common.response.ApiResponse;
import cloud.tianai.captcha.validator.common.model.dto.ImageCaptchaTrack;
import com.lihua.enums.CaptchaTypeEnum;
import com.lihua.model.web.ApiResponseModel;
import com.lihua.model.web.basecontroller.ApiResponseController;
import com.lihua.service.system.setting.SysSettingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Tag(name = "验证码")
@RestController
@RequestMapping("captcha")
public class CaptchaController extends ApiResponseController {

    @Resource
    private ImageCaptchaApplication imageCaptchaApplication;

    @Resource
    private SysSettingService sysSettingService;

    @Operation(summary = "是否启用验证码")
    @GetMapping("enable")
    public ApiResponseModel<Boolean> enable() {
        return success(sysSettingService.enableCaptcha());
    }

    @Operation(summary = "获取验证码")
    @PostMapping("get")
    public CaptchaResponse<ImageCaptchaVO> getCaptcha() {
        List<String> captchaTypes = CaptchaTypeEnum.allValue();
        // 获取随机验证码类型
        String type = captchaTypes.get(ThreadLocalRandom.current().nextInt(captchaTypes.size()));
        return imageCaptchaApplication.generateCaptcha(type);
    }

    @Operation(summary = "校验验证码")
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
