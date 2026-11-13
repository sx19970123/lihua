package com.lihua.controller;

import cloud.tianai.captcha.application.ImageCaptchaApplication;
import cloud.tianai.captcha.application.vo.ImageCaptchaVO;
import cloud.tianai.captcha.common.response.ApiResponse;
import cloud.tianai.captcha.validator.common.model.dto.ImageCaptchaTrack;
import com.lihua.captcha.enums.CaptchaTypeEnum;
import com.lihua.common.model.response.ResponseController;
import com.lihua.common.model.response.ResponseModel;
import com.lihua.web.annotation.RateLimit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Tag(name = "验证码")
@RestController
@RequestMapping("captcha")
public class CaptchaController extends ResponseController {

    @Resource
    private ImageCaptchaApplication imageCaptchaApplication;

    @Operation(summary = "获取验证码")
    @RateLimit
    @PostMapping("get")
    public ApiResponse<ImageCaptchaVO> getCaptcha() {
        // 随机获取验证码类型
        return imageCaptchaApplication.generateCaptcha(CaptchaTypeEnum.randomType());
    }

    @Operation(summary = "验证码校验")
    @RateLimit
    @PostMapping("check")
    public ApiResponse<?> checkCaptcha(@RequestBody Data data) {
        ApiResponse<?> response = imageCaptchaApplication.matching(data.getId(), data.getData());
        if (response.isSuccess()) {
            return ApiResponse.ofSuccess(Collections.singletonMap("id", data.getId()));
        }
        return response;
    }

    @Operation(summary = "获取验证码类型选项")
    @GetMapping("type/options")
    public ResponseModel<List<CaptchaTypeOption>> getCaptchaTypeOptions() {
        return success(Arrays.stream(CaptchaTypeEnum.values())
                .map(type -> new CaptchaTypeOption(type.getValue(), type.getName()))
                .toList());
    }

    // 验证码接收内部类
    @lombok.Data
    public static class Data {
        private String  id;
        private ImageCaptchaTrack data;
    }

    // 验证码类型选项
    @lombok.AllArgsConstructor
    @lombok.Data
    public static class CaptchaTypeOption {
        // 类型值
        private String value;
        // 类型名称
        private String name;
    }

}
