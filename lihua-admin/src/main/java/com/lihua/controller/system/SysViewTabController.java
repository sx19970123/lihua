package com.lihua.controller.system;

import com.lihua.annotation.Log;
import com.lihua.enums.LogTypeEnum;
import com.lihua.model.security.CurrentViewTab;
import com.lihua.model.web.ApiResponseModel;
import com.lihua.model.web.basecontroller.ApiResponseController;
import com.lihua.model.web.basecontroller.StrResponseController;
import com.lihua.entity.system.SysViewTab;
import com.lihua.service.system.viewtab.SysViewTabService;
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
public class SysViewTabController extends ApiResponseController {

    @Resource
    private SysViewTabService sysViewTabService;

    @Operation(summary = "变更收藏/锁定")
    @PostMapping
    @Log(description = "变更收藏/锁定", type = LogTypeEnum.OTHER)
    public ApiResponseModel<CurrentViewTab> save(@RequestBody @Validated SysViewTab sysViewTab) {
        return success(sysViewTabService.save(sysViewTab));
    }
}
