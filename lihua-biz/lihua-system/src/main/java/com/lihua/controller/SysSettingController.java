package com.lihua.controller;

import com.lihua.log.annotation.Log;
import com.lihua.entity.SysSetting;
import com.lihua.log.enums.LogTypeEnum;
import com.lihua.common.model.response.ApiResponseModel;
import com.lihua.common.model.response.basecontroller.ApiResponseController;
import com.lihua.service.SysSettingService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("system/setting")
public class SysSettingController extends ApiResponseController {

    @Resource
    private SysSettingService sysSettingService;

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping
    @Log(description = "保存系统配置", type = LogTypeEnum.SAVE)
    public ApiResponseModel<String> insert(@RequestBody SysSetting sysSetting) {
        return success(sysSettingService.saveSetting(sysSetting));
    }

    @GetMapping("{key}")
    @PreAuthorize("hasRole('ROLE_admin')")
    public ApiResponseModel<SysSetting> getSysSettingByKey(@PathVariable("key") String key) {
        return success(sysSettingService.getSysSettingByKey(key));
    }

    @GetMapping("defaultPassword")
    public ApiResponseModel<String> getDefaultPassword() {
        return success(sysSettingService.getDefaultPassword());
    }

    @GetMapping("base/enableCaptcha")
    public ApiResponseModel<Boolean> enableCaptcha() {
        return success(sysSettingService.enableCaptcha());
    }

    @GetMapping("base/enableGrayMode")
    public ApiResponseModel<Boolean> enableGrayMode() {
        return success(sysSettingService.enableGrayMode());
    }

    @GetMapping("base/enableSignUp")
    public ApiResponseModel<Boolean> enableSignUp() {
        return success(sysSettingService.enableSignUp());
    }
}
