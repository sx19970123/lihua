package com.lihua.system.controller;

import com.lihua.model.web.BaseController;
import com.lihua.system.entity.SysDept;
import com.lihua.system.service.SysDeptService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("system/dept")
public class SysDeptController extends BaseController {

    @Resource
    private SysDeptService sysDeptService;

    @PostMapping("list")
    public String findList(@RequestBody SysDept sysDept) {
        return success(sysDeptService.findList(sysDept));
    }

    @PostMapping
    public String save(@RequestBody @Validated SysDept sysDept) {
        return success(sysDeptService.save(sysDept));
    }

    @GetMapping("{id}")
    public String findById(@PathVariable("id") String id) {
        return success(sysDeptService.findById(id));
    }

    @DeleteMapping
    public String deleteByIds(@RequestBody @NotEmpty(message = "请选择数据") List<String> ids) {
        sysDeptService.deleteByIds(ids);
        return success();
    }
}
