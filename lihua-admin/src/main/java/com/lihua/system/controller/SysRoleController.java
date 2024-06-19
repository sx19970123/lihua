package com.lihua.system.controller;

import com.lihua.model.web.BaseController;
import com.lihua.system.entity.SysRole;
import com.lihua.system.model.SysRoleDTO;
import com.lihua.system.service.SysMenuService;
import com.lihua.system.service.SysRoleService;
import com.lihua.utils.security.LoginUserContext;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("system/role")
public class SysRoleController extends BaseController {

    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private SysMenuService sysMenuService;

    /**
     * 查询角色信息
     * @param sysRoleDTO
     * @return
     */
    @PostMapping("page")
    public String findPage(@RequestBody SysRoleDTO sysRoleDTO) {
        return success(sysRoleService.findPage(sysRoleDTO));
    }

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public String findById(@PathVariable("id") String id) {
        return success(sysRoleService.findById(id));
    }

    /**
     * 保存角色信息
     * @param sysRole
     * @return
     */
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping
    public String save(@RequestBody @Validated SysRole sysRole) {
        return success(sysRoleService.save(sysRole));
    }

    /**
     * 修改角色状态
     * @param id
     * @param currentStatus
     * @return
     */
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("updateStatus/{id}/{currentStatus}")
    public String updateStatus(@PathVariable("id") String id,@PathVariable("currentStatus") String currentStatus) {
        return success(sysRoleService.updateStatus(id, currentStatus));
    }

    /**
     * 删除角色信息
     * @param ids
     * @return
     */
    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping
    public String deleteByIds(@RequestBody @NotEmpty(message = "请选择数据") List<String> ids) {
        sysRoleService.deleteByIds(ids);
        return success();
    }

    /**
     * 获取当前用户下的角色列表
     * @return
     */
    @GetMapping("option")
    public String getRoleOption() {
        return success(LoginUserContext.getRoleList());
    }
}
