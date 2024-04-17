package com.lihua.system.controller;

import com.lihua.enums.ResultCodeEnum;
import com.lihua.model.web.BaseController;
import com.lihua.system.entity.SysDictType;
import com.lihua.system.model.SysDictTypeDTO;
import com.lihua.system.service.SysDictTypeService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("system/dictType")
public class SysDictTypeController extends BaseController {

    @Resource
    private SysDictTypeService sysDictTypeService;

    /**
     * 字典类型查询
     * @param dictTypeDTO
     * @return
     */
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("page")
    public String findPage(@RequestBody SysDictTypeDTO dictTypeDTO) {
        return success(sysDictTypeService.findPage(dictTypeDTO));
    }

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    @PreAuthorize("hasRole('ROLE_admin')")
    @GetMapping("{id}")
    public String findById(@PathVariable("id") String id) {
        return success(sysDictTypeService.findById(id));
    }

    /**
     * 保存字典类型
     * @param sysDictType
     * @return
     */
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping
    public String save(@RequestBody @Validated SysDictType sysDictType) {
        return success(sysDictTypeService.save(sysDictType));
    }

    /**
     * 删除字典类型
     * @param ids
     * @return
     */
    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping
    public String delete(@RequestBody @NotEmpty(message = "请选择数据") List<String> ids) {
        sysDictTypeService.deleteByIds(ids);
        return success();
    }

}
