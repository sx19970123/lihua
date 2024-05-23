package com.lihua.system.controller;

import com.lihua.model.web.BaseController;
import com.lihua.system.model.SysUserDTO;
import com.lihua.system.service.SysUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("system/user")
public class SysUserController extends BaseController {

    @Resource
    private SysUserService sysUserService;

    @PostMapping("page")
    public String findPage(@RequestBody SysUserDTO sysUserDTO) {
        return success(sysUserService.findPage(sysUserDTO));
    }

    @GetMapping("{id}")
    public String findById(@PathVariable("id") String id) {
        return success(sysUserService.findById(id));
    }

    @PostMapping
    public String save(@RequestBody SysUserDTO sysUserDTO) {
        return success(sysUserService.save(sysUserDTO));
    }

    @DeleteMapping
    public String deleteByIds(@RequestBody List<String> ids) {
        sysUserService.deleteByIds(ids);
        return success();
    }
}
