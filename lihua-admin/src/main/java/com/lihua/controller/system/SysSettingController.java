package com.lihua.controller.system;

import com.lihua.annotation.Log;
import com.lihua.enums.LogTypeEnum;
import com.lihua.model.web.BaseController;
import com.lihua.entity.system.SysSetting;
import com.lihua.service.system.setting.SysSettingService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("system/setting")
public class SysSettingController extends BaseController {

    @Resource
    private SysSettingService sysSettingService;

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping
    @Log(description = "保存系统配置", type = LogTypeEnum.SAVE)
    public String insert(@RequestBody SysSetting sysSetting) {
        return success(sysSettingService.insert(sysSetting));
    }

    @GetMapping("{componentName}")
    public String querySysSettingByComponentName(@PathVariable("componentName") String componentName) {
        return success(sysSettingService.getSysSettingByComponentName(componentName));
    }
}
