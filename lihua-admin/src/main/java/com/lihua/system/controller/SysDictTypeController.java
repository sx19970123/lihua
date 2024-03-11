package com.lihua.system.controller;

import com.lihua.enums.ResultCodeEnum;
import com.lihua.model.web.BaseController;
import com.lihua.system.entity.SysDictType;
import com.lihua.system.model.SysDictTypeDTO;
import com.lihua.system.service.SysDictTypeService;
import jakarta.annotation.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("system/dictType")
public class SysDictTypeController extends BaseController {

    @Resource
    private SysDictTypeService sysDictTypeService;

    @PostMapping("page")
    public String findPage(@RequestBody SysDictTypeDTO dictTypeDTO) {
        return success(sysDictTypeService.findPage(dictTypeDTO));
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") String id) {
        if (!StringUtils.hasText(id)) {
            return error(ResultCodeEnum.PRIMARY_KEY_IS_EMPTY);
        }
        return success(sysDictTypeService.findById(id));
    }

    @PostMapping
    public String save(@RequestBody SysDictType sysDictType) {
        return success(sysDictTypeService.save(sysDictType));
    }

    @DeleteMapping
    public String delete(@RequestBody List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return error(ResultCodeEnum.PRIMARY_KEY_COLLECTION_IS_EMPTY);
        }
        sysDictTypeService.deleteByIds(ids);
        return success();
    }

}
