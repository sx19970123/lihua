package com.lihua.system.controller;

import com.lihua.enums.ResultCodeEnum;
import com.lihua.model.web.BaseController;
import com.lihua.system.entity.SysDictData;
import com.lihua.system.model.SysDictDataDTO;
import com.lihua.system.service.SysDictDataService;
import jakarta.annotation.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("system/dictData")
public class SysDictDataController extends BaseController {

    @Resource
    private SysDictDataService sysDictDataService;

    @PostMapping("list")
    public String findListByTypeId(@RequestBody SysDictDataDTO dictDataDTO) {
        if (!StringUtils.hasText(dictDataDTO.getDictTypeId())) {
            return error(ResultCodeEnum.ERROR,"数据字典类型id为空");
        }
        return success(sysDictDataService.findList(dictDataDTO));
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") String id) {
        if (!StringUtils.hasText(id)) {
            return error(ResultCodeEnum.PRIMARY_KEY_IS_EMPTY);
        }
        return success(sysDictDataService.findById(id));
    }

    @PostMapping
    public String save(@RequestBody SysDictData sysDictData) {
        if (!StringUtils.hasText(sysDictData.getValue())) {
            return error(ResultCodeEnum.PARAMS_IS_EMPTY,"字典值不存在");
        }
        if (!StringUtils.hasText(sysDictData.getLabel())) {
            return error(ResultCodeEnum.PARAMS_IS_EMPTY,"字典标签不存在");
        }
        if (!StringUtils.hasText(sysDictData.getDictTypeId())) {
            return error(ResultCodeEnum.PARAMS_IS_EMPTY,"字典类型不存在");
        }
        if (sysDictData.getSort() == null) {
            return error(ResultCodeEnum.PARAMS_IS_EMPTY,"字典排序不存在");
        }
        return success(sysDictDataService.save(sysDictData));
    }

    @DeleteMapping
    public String delete(@RequestBody List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return error(ResultCodeEnum.PRIMARY_KEY_COLLECTION_IS_EMPTY);
        }
        sysDictDataService.deleteByIds(ids);
        return success();
    }
}
