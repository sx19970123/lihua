package com.lihua.controller;

import com.lihua.controller.base.BaseSysSettingController;
import com.lihua.common.model.response.ApiResponseModel;
import com.lihua.entity.SysSetting;
import com.lihua.log.annotation.Log;
import com.lihua.log.enums.LogTypeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "系统设置")
@RestController
@RequestMapping("system/setting")
public class SysSettingController extends BaseSysSettingController {

    @Operation(summary = "保存系统设置")
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping
    @Log(description = "保存系统设置", type = LogTypeEnum.SAVE)
    public ApiResponseModel<String> insert(@RequestBody SysSetting sysSetting) {
        return success(sysSettingService.saveSetting(sysSetting));
    }

    @Operation(summary = "根据key获取系统设置")
    @GetMapping("{key}")
    @PreAuthorize("hasRole('ROLE_admin')")
    public ApiResponseModel<SysSetting> getSysSettingByKey(@PathVariable("key") String key) {
        return success(sysSettingService.getSysSettingByKey(key));
    }

    @Operation(summary = "获取默认密码")
    @GetMapping("defaultPassword")
    @PreAuthorize("hasRole('ROLE_admin')")
    public ApiResponseModel<String> getDefaultPassword() {
        return success(sysSettingService.getDefaultPassword());
    }

    @Operation(summary = "获取是否开启灰色模式")
    @GetMapping("base/enableGrayMode")
    public ApiResponseModel<Boolean> enableGrayMode() {
        return success(sysSettingService.enableGrayMode());
    }
}
