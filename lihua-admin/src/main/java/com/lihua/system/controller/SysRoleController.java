package com.lihua.system.controller;

import com.lihua.enums.ResultCodeEnum;
import com.lihua.model.web.BaseController;
import com.lihua.system.entity.SysRole;
import com.lihua.system.model.SysRoleDTO;
import com.lihua.system.service.SysMenuService;
import com.lihua.system.service.SysRoleService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("system/role")
public class SysRoleController extends BaseController {

    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private SysMenuService sysMenuService;

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("page")
    public String findPage(@RequestBody SysRoleDTO sysRoleDTO) {
        return success(sysRoleService.findPage(sysRoleDTO));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @GetMapping("{id}")
    public String findById(@PathVariable("id") String id) {
        if (!StringUtils.hasText(id)) {
            return error(ResultCodeEnum.PRIMARY_KEY_IS_EMPTY);
        }
        return success(sysRoleService.findById(id));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping
    public String save(@RequestBody SysRole sysRole) {
        return success(sysRoleService.save(sysRole));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping
    public String deleteByIds(@RequestBody List<String> ids) {
        if (ids.isEmpty()) {
            return error(ResultCodeEnum.PRIMARY_KEY_COLLECTION_IS_EMPTY);
        }

        sysRoleService.deleteByIds(ids);
        return success();
    }
}
