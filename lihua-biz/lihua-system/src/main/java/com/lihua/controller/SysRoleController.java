package com.lihua.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.annotation.Log;
import com.lihua.entity.SysRole;
import com.lihua.enums.LogTypeEnum;
import com.lihua.manager.LoginUserContext;
import com.lihua.model.CurrentRole;
import com.lihua.model.dto.SysRoleDTO;
import com.lihua.model.validation.MaxPageSizeLimit;
import com.lihua.model.web.ApiResponseModel;
import com.lihua.model.web.basecontroller.ApiResponseController;
import com.lihua.service.SysMenuService;
import com.lihua.service.SysRoleService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("system/role")
@Validated
public class SysRoleController extends ApiResponseController {

    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private SysMenuService sysMenuService;

    @PostMapping("page")
    public ApiResponseModel<IPage<SysRole>> queryPage(@RequestBody @Validated(MaxPageSizeLimit.class) SysRoleDTO sysRoleDTO) {
        return success(sysRoleService.queryPage(sysRoleDTO));
    }

    @GetMapping("{id}")
    public ApiResponseModel<SysRole> queryById(@PathVariable("id") String id) {
        return success(sysRoleService.queryById(id));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping
    @Log(description = "保存角色信息", type = LogTypeEnum.SAVE)
    public ApiResponseModel<String> save(@RequestBody @Validated SysRole sysRole) {
        return success(sysRoleService.save(sysRole));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("updateStatus/{id}/{currentStatus}")
    @Log(description = "更新角色状态", type = LogTypeEnum.UPDATE_STATUS)
    public ApiResponseModel<String> updateStatus(@PathVariable("id") String id, @PathVariable("currentStatus") String currentStatus) {
        return success(sysRoleService.updateStatus(id, currentStatus));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping
    @Log(description = "删除角色数据", type = LogTypeEnum.DELETE)
    public ApiResponseModel<String> deleteByIds(@RequestBody @NotEmpty(message = "请选择数据") List<String> ids) {
        sysRoleService.deleteByIds(ids);
        return success();
    }

    @GetMapping("option")
    public ApiResponseModel<List<CurrentRole>> getRoleOption() {
        return success(LoginUserContext.getRoleList().stream().filter(role -> !"ROLE_admin".equals(role.getCode())).toList());
    }
}
