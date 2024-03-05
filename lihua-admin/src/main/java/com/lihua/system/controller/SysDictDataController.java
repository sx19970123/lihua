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

    @GetMapping("page")
    public String findPage(@RequestBody SysDictDataDTO dictDataDTO) {
        return success(sysDictDataService.findPage(dictDataDTO));
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
        return success(sysDictDataService.save(sysDictData));
    }

    @DeleteMapping
    public String delete(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return error(ResultCodeEnum.PRIMARY_KEY_COLLECTION_IS_EMPTY);
        }
        sysDictDataService.deleteByIds(ids);
        return success();
    }
}
