package com.lihua.controller;

import com.lihua.common.model.response.ResponseModel;
import com.lihua.common.model.response.ResponseController;
import com.lihua.entity.SysViewTab;
import com.lihua.log.annotation.Log;
import com.lihua.log.enums.LogTypeEnum;
import com.lihua.security.model.CurrentViewTab;
import com.lihua.service.SysViewTabService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "多任务栏")
@RestController
@RequestMapping("system/viewTab")
public class SysViewTabController extends ResponseController {

    @Resource
    private SysViewTabService sysViewTabService;

    @Operation(summary = "变更收藏、锁定")
    @PostMapping
    @Log(description = "变更收藏/锁定", type = LogTypeEnum.OTHER)
    public ResponseModel<CurrentViewTab> save(@RequestBody @Validated SysViewTab sysViewTab) {
        return success(sysViewTabService.save(sysViewTab));
    }
}
