package com.lihua.controller.system;

import com.lihua.annotation.Log;
import com.lihua.enums.LogTypeEnum;
import com.lihua.enums.ResultCodeEnum;
import com.lihua.model.dict.SysDictDataVO;
import com.lihua.model.web.ApiResponseModel;
import com.lihua.model.web.basecontroller.ApiResponseController;
import com.lihua.entity.system.SysDictData;
import com.lihua.model.system.dto.SysDictDataDTO;
import com.lihua.service.system.dict.SysDictDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "字段数据")
@RestController
@RequestMapping("system/dictData")
@Validated
public class SysDictDataController extends ApiResponseController {

    @Resource
    private SysDictDataService sysDictDataService;

    @Operation(summary = "查询字典数据列表")
    @PostMapping("list")
    public ApiResponseModel<List<SysDictData>> queryListByTypeCode(@RequestBody SysDictDataDTO dictDataDTO) {
        if (!StringUtils.hasText(dictDataDTO.getDictTypeCode())) {
            return error(ResultCodeEnum.ERROR,"数据字典类型id为空");
        }
        return success(sysDictDataService.queryList(dictDataDTO));
    }

    @Operation(summary = "查询下拉框中字典选项")
    @GetMapping("option/{dictTypeCode}")
    public ApiResponseModel<List<SysDictDataVO>> queryDictOptionList(@PathVariable("dictTypeCode") String dictTypeCode) {
        return success(sysDictDataService.queryDictOptionList(dictTypeCode));
    }

    @Operation(summary = "获取字典数据option")
    @PostMapping("option")
    public ApiResponseModel<Map<String, List<SysDictDataVO>>> queryDictOptionList(@RequestBody List<String> dictTypeCodeList) {
        return success(sysDictDataService.queryDictOptionList(dictTypeCodeList));
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
    public ApiResponseModel<String> delete(@RequestBody @NotEmpty(message = "请选择字段数据") List<String> ids) {
        sysDictDataService.deleteByIds(ids);
        return success();
    }
}
