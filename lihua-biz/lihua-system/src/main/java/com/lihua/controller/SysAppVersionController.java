package com.lihua.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.common.model.response.ApiResponseModel;
import com.lihua.common.model.response.basecontroller.ApiResponseController;
import com.lihua.entity.SysAppVersion;
import com.lihua.log.annotation.Log;
import com.lihua.log.enums.LogTypeEnum;
import com.lihua.model.dto.SysAppVersionDTO;
import com.lihua.mybatis.model.validation.MaxPageSizeLimit;
import com.lihua.service.SysAppVersionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "App版本管理")
@RestController
@RequestMapping("system/app-version")
@Validated
public class SysAppVersionController extends ApiResponseController {

    @Resource
    private SysAppVersionService sysAppVersionService;

    @Operation(summary = "分页查询")
    @PostMapping("page")
    public ApiResponseModel<IPage<SysAppVersion>> queryPage(@RequestBody @Validated(MaxPageSizeLimit.class) SysAppVersionDTO dto) {
        return success(sysAppVersionService.queryPage(dto));
    }

    @Operation(summary = "根据id查询详情")
    @GetMapping("{id}")
    public ApiResponseModel<SysAppVersion> queryById(@PathVariable("id") String id) {
        return success(sysAppVersionService.queryById(id));
    }

    @Operation(summary = "保存App版本")
    @PostMapping("save")
    @Log(description = "保存App版本", type = LogTypeEnum.SAVE)
    public ApiResponseModel<String> save(@RequestBody @Validated SysAppVersionDTO dto) {
        return success(sysAppVersionService.saveOrUpdate(dto));
    }

    @Operation(summary = "发布App版本")
    @PostMapping("publish/{id}")
    @Log(description = "发布App版本", type = LogTypeEnum.OTHER)
    public ApiResponseModel<String> publish(@PathVariable("id") String id) {
        return success(sysAppVersionService.publish(id));
    }

    @Operation(summary = "下线App版本")
    @PostMapping("offline/{id}")
    @Log(description = "下线App版本", type = LogTypeEnum.OTHER)
    public ApiResponseModel<String> offline(@PathVariable("id") String id) {
        return success(sysAppVersionService.offline(id));
    }

    @Operation(summary = "删除App版本")
    @DeleteMapping
    @Log(description = "删除App版本", type = LogTypeEnum.DELETE)
    public ApiResponseModel<String> deleteByIds(@RequestBody @NotEmpty(message = "请选择数据") List<String> ids) {
        sysAppVersionService.deleteByIds(ids);
        return success();
    }
}
