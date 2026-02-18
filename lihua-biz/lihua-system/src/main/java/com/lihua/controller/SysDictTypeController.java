package com.lihua.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.annotation.Log;
import com.lihua.entity.SysDictType;
import com.lihua.enums.LogTypeEnum;
import com.lihua.model.dto.SysDictTypeDTO;
import com.lihua.service.SysDictTypeService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import com.lihua.model.validation.MaxPageSizeLimit;
import com.lihua.model.web.ApiResponseModel;
import com.lihua.model.web.basecontroller.ApiResponseController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("system/dictType")
@Validated
public class SysDictTypeController extends ApiResponseController {

    @Resource
    private SysDictTypeService sysDictTypeService;

    @PostMapping("page")
    public ApiResponseModel<IPage<SysDictType>> queryPage(@RequestBody @Validated(MaxPageSizeLimit.class) SysDictTypeDTO dictTypeDTO) {
        return success(sysDictTypeService.queryPage(dictTypeDTO));
    }

    @GetMapping("{id}")
    public ApiResponseModel<SysDictType> queryById(@PathVariable("id") String id) {
        return success(sysDictTypeService.queryById(id));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping
    @Log(description = "保存字典类型", type = LogTypeEnum.SAVE)
    public ApiResponseModel<String> save(@RequestBody @Validated SysDictType sysDictType) {
        return success(sysDictTypeService.save(sysDictType));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("updateStatus/{id}/{currentStatus}")
    @Log(description = "修在字典类型状态", type = LogTypeEnum.UPDATE_STATUS)
    public ApiResponseModel<String> updateStatus(@PathVariable("id") String id, @PathVariable("currentStatus") String currentStatus) {
        return success(sysDictTypeService.updateStatus(id, currentStatus));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping
    @Log(description = "删除字典类型数据", type = LogTypeEnum.DELETE)
    public ApiResponseModel<String> delete(@RequestBody @NotEmpty(message = "请选择数据") List<String> ids) {
        sysDictTypeService.deleteByIds(ids);
        return success();
    }

    @PostMapping("reload/cache")
    public ApiResponseModel<String> reloadCache() {
        sysDictTypeService.reloadCache();
        return success();
    }

}
