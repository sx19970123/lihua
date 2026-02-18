package com.lihua.controller;

import com.lihua.annotation.Log;
import com.lihua.entity.SysViewTab;
import com.lihua.enums.LogTypeEnum;
import com.lihua.model.CurrentViewTab;
import com.lihua.model.web.ApiResponseModel;
import com.lihua.model.web.basecontroller.ApiResponseController;
import com.lihua.service.SysViewTabService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("system/viewTab")
public class SysViewTabController extends ApiResponseController {

    @Resource
    private SysViewTabService sysViewTabService;

    @PostMapping
    @Log(description = "变更收藏/锁定", type = LogTypeEnum.OTHER)
    public ApiResponseModel<CurrentViewTab> save(@RequestBody @Validated SysViewTab sysViewTab) {
        return success(sysViewTabService.save(sysViewTab));
    }
}
