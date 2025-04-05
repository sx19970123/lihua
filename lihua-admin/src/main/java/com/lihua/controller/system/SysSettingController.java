package com.lihua.controller.system;

import com.lihua.annotation.Log;
import com.lihua.enums.LogTypeEnum;
import com.lihua.model.web.ApiResponseModel;
import com.lihua.model.web.basecontroller.ApiResponseController;
import com.lihua.entity.system.SysSetting;
import com.lihua.service.system.setting.SysSettingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "系统设置")
@RestController
@RequestMapping("system/setting")
public class SysSettingController extends ApiResponseController {

    @Resource
    private SysSettingService sysSettingService;

    @Operation(summary = "保存系统配置")
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping
    @Log(description = "保存系统配置", type = LogTypeEnum.SAVE)
    public ApiResponseModel<String> insert(@RequestBody SysSetting sysSetting) {
        return success(sysSettingService.insert(sysSetting));
    }

    @Operation(summary = "根据组件名称获取配置")
    @GetMapping("{componentName}")
    public ApiResponseModel<SysSetting> querySysSettingByComponentName(@PathVariable("componentName") String componentName) {
        return success(sysSettingService.getSysSettingByComponentName(componentName));
    }
}
