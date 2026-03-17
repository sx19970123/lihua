package com.lihua.controller;

import com.lihua.common.model.response.ApiResponseModel;
import com.lihua.common.model.response.basecontroller.ApiResponseController;
import com.lihua.common.utils.json.JsonUtils;
import com.lihua.entity.SysMenu;
import com.lihua.log.annotation.Log;
import com.lihua.log.enums.LogTypeEnum;
import com.lihua.model.validation.MenuValidation;
import com.lihua.service.SysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "菜单管理")
@Slf4j
@Validated
@RestController
@RequestMapping("system/menu")
public class SysMenuController extends ApiResponseController {

    @Resource
    private SysMenuService sysMenuService;

    @Operation(summary = "列表查询")
    @PostMapping("list")
    public ApiResponseModel<List<SysMenu>> queryList(@RequestBody SysMenu sysMenu) {
        return success(sysMenuService.queryList(sysMenu));
    }

    @Operation(summary = "根据id查询详情")
    @GetMapping("{id}")
    public ApiResponseModel<SysMenu> queryById(@PathVariable("id") @NotNull(message = "请选择数据") String id) {
        return success(sysMenuService.queryById(id));
    }

    @Operation(summary = "保存菜单")
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("directory")
    @Log(description = "保存菜单数据", type = LogTypeEnum.SAVE)
    public ApiResponseModel<String> saveDirectory(@RequestBody @Validated(MenuValidation.MenuDirectoryValidation.class) SysMenu sysMenu) {
        return success(sysMenuService.save(sysMenu));
    }

    @Operation(summary = "保存页面")
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("page")
    @Log(description = "保存页面数据", type = LogTypeEnum.SAVE)
    public ApiResponseModel<String> savePage(@RequestBody @Validated(MenuValidation.MenuPageValidation.class) SysMenu sysMenu) {
        // 校验 query 是否为json参数
        if (StringUtils.hasText(sysMenu.getQuery())) {
            JsonUtils.isJson(sysMenu.getQuery());
        }

        return success(sysMenuService.save(sysMenu));
    }

    @Operation(summary = "保存链接")
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("link")
    @Log(description = "保存链接数据", type = LogTypeEnum.SAVE)
    public ApiResponseModel<String> saveLink(@RequestBody @Validated(MenuValidation.MenuLinkValidation.class) SysMenu sysMenu) {
        return success(sysMenuService.save(sysMenu));
    }

    @Operation(summary = "保存权限")
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("perms")
    @Log(description = "保存权限数据", type = LogTypeEnum.SAVE)
    public ApiResponseModel<String> savePerms(@RequestBody @Validated(MenuValidation.MenuPermsValidation.class) SysMenu sysMenu) {
        return success(sysMenuService.save(sysMenu));
    }

    @Operation(summary = "修改状态")
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("updateStatus/{currentStatus}")
    @Log(description = "更新菜单状态", type = LogTypeEnum.UPDATE_STATUS)
    public ApiResponseModel<String> updateStatus(@PathVariable("currentStatus") String currentStatus, @RequestBody List<String> ids) {
        return success(sysMenuService.updateStatus(ids, currentStatus));
    }

    @Operation(summary = "批量删除")
    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping
    @Log(description = "删除菜单数据", type = LogTypeEnum.DELETE)
    public ApiResponseModel<String> deleteByIds(@RequestBody @NotEmpty(message = "请选择数据") List<String> ids) {
        sysMenuService.deleteByIds(ids);
        return success();
    }

    @Operation(summary = "获取菜单树选项")
    @GetMapping("option")
    public ApiResponseModel<List<SysMenu>> menuTreeOption() {
        return success(sysMenuService.menuTreeOption());
    }
}
