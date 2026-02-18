package com.lihua.controller;

import com.lihua.annotation.Log;
import com.lihua.entity.SysSetting;
import com.lihua.enums.LogTypeEnum;
import com.lihua.model.web.ApiResponseModel;
import com.lihua.model.web.basecontroller.ApiResponseController;
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
        return success(sysSettingService.insert(sysSetting));
    }

    @GetMapping("{componentName}")
    public ApiResponseModel<SysSetting> querySysSettingByComponentName(@PathVariable("componentName") String componentName) {
        return success(sysSettingService.getSysSettingByComponentName(componentName));
    }
}
