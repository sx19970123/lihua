package com.lihua.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.annotation.Log;
import com.lihua.enums.LogTypeEnum;
import com.lihua.model.validation.MaxPageSizeLimit;
import com.lihua.model.web.ApiResponseModel;
import com.lihua.model.web.basecontroller.ApiResponseController;
import com.lihua.entity.system.SysDictType;
import com.lihua.model.system.dto.SysDictTypeDTO;
import com.lihua.service.system.dict.SysDictTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "字典管理")
@RestController
@RequestMapping("system/dictType")
@Validated
public class SysDictTypeController extends ApiResponseController {

    @Resource
    private SysDictTypeService sysDictTypeService;

    @Operation(summary = "字典列表查询")
    @PostMapping("page")
    public ApiResponseModel<IPage<SysDictType>> queryPage(@RequestBody @Validated(MaxPageSizeLimit.class) SysDictTypeDTO dictTypeDTO) {
        return success(sysDictTypeService.queryPage(dictTypeDTO));
    }

    @Operation(summary = "根据主键查询")
    @GetMapping("{id}")
    public ApiResponseModel<SysDictType> queryById(@PathVariable("id") String id) {
        return success(sysDictTypeService.queryById(id));
    }

    @Operation(summary = "保存字典类型")
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping
    @Log(description = "保存字典类型", type = LogTypeEnum.SAVE)
    public ApiResponseModel<String> save(@RequestBody @Validated SysDictType sysDictType) {
        return success(sysDictTypeService.save(sysDictType));
    }

    @Operation(summary = "修改字典状态")
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("updateStatus/{id}/{currentStatus}")
    @Log(description = "修在字典类型状态", type = LogTypeEnum.UPDATE_STATUS)
    public ApiResponseModel<String> updateStatus(@PathVariable("id") String id, @PathVariable("currentStatus") String currentStatus) {
        return success(sysDictTypeService.updateStatus(id, currentStatus));
    }

    @Operation(summary = "删除字典类型")
    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping
    @Log(description = "删除字典类型数据", type = LogTypeEnum.DELETE)
    public ApiResponseModel<String> delete(@RequestBody @NotEmpty(message = "请选择数据") List<String> ids) {
        sysDictTypeService.deleteByIds(ids);
        return success();
    }

    @Operation(summary = "刷新字典缓存")
    @PostMapping("reload/cache")
    public ApiResponseModel<String> reloadCache() {
        sysDictTypeService.reloadCache();
        return success();
    }

}
