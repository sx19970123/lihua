package com.lihua.controller;

import com.lihua.log.annotation.Log;
import com.lihua.entity.SysViewTab;
import com.lihua.log.enums.LogTypeEnum;
import com.lihua.security.model.CurrentViewTab;
import com.lihua.common.model.response.ApiResponseModel;
import com.lihua.common.model.response.basecontroller.ApiResponseController;
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
