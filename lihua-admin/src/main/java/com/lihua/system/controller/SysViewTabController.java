package com.lihua.system.controller;

import com.lihua.model.web.BaseController;
import com.lihua.system.entity.SysViewTab;
import com.lihua.system.service.SysViewTabService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("system/viewTab")
public class SysViewTabController extends BaseController {

    @Resource
    private SysViewTabService sysViewTabService;

    @PostMapping
    public String save(@RequestBody @Validated SysViewTab sysViewTab) {
        return success(sysViewTabService.save(sysViewTab));
    }
}
