package com.lihua.system.controller;

import com.lihua.annotation.Log;
import com.lihua.enums.LogTypeEnum;
import com.lihua.model.validation.MaxPageSizeLimit;
import com.lihua.model.web.BaseController;
import com.lihua.system.entity.SysRole;
import com.lihua.system.model.dto.SysRoleDTO;
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
@Validated
public class SysRoleController extends BaseController {

    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private SysMenuService sysMenuService;

    /**
     * 查询角色信息
     */
    @PostMapping("page")
    public String findPage(@RequestBody @Validated(MaxPageSizeLimit.class) SysRoleDTO sysRoleDTO) {
        return success(sysRoleService.findPage(sysRoleDTO));
    }

    /**
     * 根据主键查询
     */
    @GetMapping("{id}")
    public String findById(@PathVariable("id") String id) {
        return success(sysRoleService.findById(id));
    }

    /**
     * 保存角色信息
     */
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping
    @Log(description = "保存角色信息", type = LogTypeEnum.SAVE)
    public String save(@RequestBody @Validated SysRole sysRole) {
        return success(sysRoleService.save(sysRole));
    }

    /**
     * 修改角色状态
     */
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("updateStatus/{id}/{currentStatus}")
    @Log(description = "更新角色状态", type = LogTypeEnum.UPDATE_STATUS)
    public String updateStatus(@PathVariable("id") String id,@PathVariable("currentStatus") String currentStatus) {
        return success(sysRoleService.updateStatus(id, currentStatus));
    }

    /**
     * 删除角色信息
     */
    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping
    @Log(description = "删除角色数据", type = LogTypeEnum.DELETE)
    public String deleteByIds(@RequestBody @NotEmpty(message = "请选择数据") List<String> ids) {
        sysRoleService.deleteByIds(ids);
        return success();
    }

    /**
     * 获取当前用户下的角色列表
     */
    @GetMapping("option")
    public String getRoleOption() {
        return success(LoginUserContext.getRoleList().stream().filter(role -> !"ROLE_admin".equals(role.getCode())).toList());
    }
}
