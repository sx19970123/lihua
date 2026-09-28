package com.lihua.controller;

import com.lihua.controller.base.BaseSysDictDataController;
import com.lihua.common.model.response.ApiResponseModel;
import com.lihua.entity.SysDictData;
import com.lihua.log.annotation.Log;
import com.lihua.log.enums.LogTypeEnum;
import com.lihua.model.dto.SysDictDataDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "字典数据")
@Validated
@RestController
@RequestMapping("system/dictData")
public class SysDictDataController extends BaseSysDictDataController {

    @Operation(summary = "列表查询")
    @PostMapping("list")
    public ApiResponseModel<List<SysDictData>> queryListByTypeCode(@RequestBody @Validated SysDictDataDTO dictDataDTO) {
        return success(sysDictDataService.queryList(dictDataDTO));
    }

    @Operation(summary = "保存字典数据")
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping
    @Log(description = "保存字典数据", type = LogTypeEnum.SAVE)
    public ApiResponseModel<String> save(@RequestBody @Validated SysDictData sysDictData) {
        return success(sysDictDataService.save(sysDictData));
    }

    @Operation(summary = "删除字典数据")
    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping
    @Log(description = "删除字典数据", type = LogTypeEnum.DELETE)
    public ApiResponseModel<String> delete(@RequestBody @NotEmpty(message = "请选中要删除的数据") List<String> ids) {
        sysDictDataService.deleteByIds(ids);
        return success();
    }
}
