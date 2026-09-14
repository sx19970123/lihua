package com.lihua.controller.base;

import com.lihua.common.model.response.ApiResponseModel;
import com.lihua.common.model.response.basecontroller.ApiResponseController;
import com.lihua.service.SysSettingService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 系统设置控制器基类：承载管理版与 App 版共有端点（对外行为双版本一致，差异仅在路由前缀与文档分组）；
 * 设置的保存与读取类端点仅管理版提供，由管理版子类自行声明
 */
public abstract class BaseSysSettingController extends ApiResponseController {

    @Resource
    protected SysSettingService sysSettingService;

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
