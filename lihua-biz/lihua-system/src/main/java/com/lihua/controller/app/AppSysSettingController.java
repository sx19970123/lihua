package com.lihua.controller.app;

import com.lihua.common.model.response.ApiResponseModel;
import com.lihua.common.model.response.basecontroller.ApiResponseController;
import com.lihua.service.SysSettingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "APP-系统设置")
@RequestMapping("app/system/setting")
@RestController
public class AppSysSettingController extends ApiResponseController {

    @Resource
    private SysSettingService sysSettingService;

    @Operation(summary = "获取是否开启验证码")
    @GetMapping("base/enableCaptcha")
    public ApiResponseModel<Boolean> enableCaptcha() {
        return success(sysSettingService.enableCaptcha());
    }

    @Operation(summary = "获取是否开启自助注册")
    @GetMapping("base/enableSignUp")
    public ApiResponseModel<Boolean> enableSignUp() {
        return success(sysSettingService.enableSignUp());
    }
}
