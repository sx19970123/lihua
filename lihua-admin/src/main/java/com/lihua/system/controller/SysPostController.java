package com.lihua.system.controller;

import com.lihua.model.web.BaseController;
import com.lihua.system.entity.SysDept;
import com.lihua.system.entity.SysPost;
import com.lihua.system.model.SysPostDTO;
import com.lihua.system.service.SysPostService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("system/post")
public class SysPostController extends BaseController {

    @Resource
    private SysPostService sysPostService;


    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("page")
    public String findPage(@RequestBody SysPostDTO dto) {
        return success(sysPostService.findPage(dto));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @GetMapping("{id}")
    public String findById(@PathVariable("id") String id) {
        return success(sysPostService.findById(id));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping
    public String save(@RequestBody @Validated SysPost sysPost) {
        return success(sysPostService.save(sysPost));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping
    public String deleteByIds(@RequestBody @NotEmpty(message = "请选中要删除的数据") List<String> ids) {
        sysPostService.deleteByIds(ids);
        return success();
    }

    @PostMapping("option")
    public String getPostOptionByDeptId(@RequestBody @NotEmpty(message = "部门集合为空") List<String> deptIds) {
        return success(sysPostService.getPostOptionByDeptId(deptIds));
    }

}
