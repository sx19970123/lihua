package com.lihua.system.controller;

import com.lihua.annotation.Log;
import com.lihua.enums.LogTypeEnum;
import com.lihua.model.validation.MaxPageSizeLimit;
import com.lihua.model.web.BaseController;
import com.lihua.system.entity.SysDictType;
import com.lihua.system.model.dto.SysDictTypeDTO;
import com.lihua.system.service.SysDictTypeService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("system/dictType")
@Validated
public class SysDictTypeController extends BaseController {

    @Resource
    private SysDictTypeService sysDictTypeService;

    /**
     * 字典类型查询
     */
    @PostMapping("page")
    public String findPage(@RequestBody @Validated(MaxPageSizeLimit.class) SysDictTypeDTO dictTypeDTO) {
        return success(sysDictTypeService.findPage(dictTypeDTO));
    }

    /**
     * 根据主键查询
     */
    @GetMapping("{id}")
    public String findById(@PathVariable("id") String id) {
        return success(sysDictTypeService.findById(id));
    }

    /**
     * 保存字典类型
     */
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping
    @Log(description = "保存字典类型", type = LogTypeEnum.SAVE)
    public String save(@RequestBody @Validated SysDictType sysDictType) {
        return success(sysDictTypeService.save(sysDictType));
    }

    /**
     * 修改字典状态
     */
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("updateStatus/{id}/{currentStatus}")
    @Log(description = "修在字典类型状态", type = LogTypeEnum.UPDATE_STATUS)
    public String updateStatus(@PathVariable("id") String id,@PathVariable("currentStatus") String currentStatus) {
        return success(sysDictTypeService.updateStatus(id, currentStatus));
    }

    /**
     * 删除字典类型
     */
    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping
    @Log(description = "删除字典类型数据", type = LogTypeEnum.DELETE)
    public String delete(@RequestBody @NotEmpty(message = "请选择数据") List<String> ids) {
        sysDictTypeService.deleteByIds(ids);
        return success();
    }

    @PostMapping("reload/cache")
    public String reloadCache() {
        sysDictTypeService.reloadCache();
        return success();
    }

}
