package com.lihua.system.controller;

import com.lihua.annotation.Log;
import com.lihua.enums.LogTypeEnum;
import com.lihua.model.web.BaseController;
import com.lihua.system.entity.SysSetting;
import com.lihua.system.service.SysSettingService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("system/setting")
public class SysSettingController extends BaseController {

    @Resource
    private SysSettingService sysSettingService;

    @PostMapping
    @Log(description = "新增系统配置", type = LogTypeEnum.SAVE)
    public String insert(@RequestBody SysSetting sysSetting) {
        return success(sysSettingService.insert(sysSetting));
    }

    @GetMapping
    public String findList() {
        return success(sysSettingService.findList());
    }

}
