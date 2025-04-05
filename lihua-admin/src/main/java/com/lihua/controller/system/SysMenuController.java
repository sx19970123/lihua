package com.lihua.controller.system;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lihua.annotation.Log;
import com.lihua.enums.LogTypeEnum;
import com.lihua.enums.ResultCodeEnum;
import com.lihua.model.web.ApiResponseModel;
import com.lihua.model.web.basecontroller.ApiResponseController;
import com.lihua.entity.system.SysMenu;
import com.lihua.model.system.validation.MenuValidation;
import com.lihua.service.system.menu.SysMenuService;
import com.lihua.utils.json.JsonUtils;
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
@RestController
@RequestMapping("system/menu")
@Slf4j
@Validated
public class SysMenuController extends ApiResponseController {

    @Resource
    private SysMenuService sysMenuService;

    @Operation(summary = "列表页查询")
    @PostMapping("list")
    public ApiResponseModel<List<SysMenu>> queryList(@RequestBody SysMenu sysMenu) {
        return success(sysMenuService.queryList(sysMenu));
    }

    @Operation(summary = "根据主键查询")
    @GetMapping("{id}")
    public ApiResponseModel<SysMenu> queryById(@PathVariable("id") @NotNull(message = "请选择数据") String id) {
        return success(sysMenuService.queryById(id));
    }

    @Operation(summary = "保存菜单数据")
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("directory")
    @Log(description = "保存菜单数据", type = LogTypeEnum.SAVE)
    public ApiResponseModel<String> saveDirectory(@RequestBody @Validated(MenuValidation.MenuDirectoryValidation.class) SysMenu sysMenu) {
        return success(sysMenuService.save(sysMenu));
    }

    @Operation(summary = "保存页面数据")
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("page")
    @Log(description = "保存页面数据", type = LogTypeEnum.SAVE)
    public ApiResponseModel<String> savePage(@RequestBody @Validated(MenuValidation.MenuPageValidation.class) SysMenu sysMenu) {
        // 校验 query 是否为json参数
        if (StringUtils.hasText(sysMenu.getQuery())) {
            try {
                JsonUtils.isJson(sysMenu.getQuery().replace("\"",",,,"));
            } catch (JsonProcessingException e) {
                return error(ResultCodeEnum.PARAMS_ERROR);
            }
        }

        return success(sysMenuService.save(sysMenu));
    }

    @Operation(summary = "保存链接数据")
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("link")
    @Log(description = "保存链接数据", type = LogTypeEnum.SAVE)
    public ApiResponseModel<String> saveLink(@RequestBody @Validated(MenuValidation.MenuLinkValidation.class) SysMenu sysMenu) {
        return success(sysMenuService.save(sysMenu));
    }

    @Operation(summary = "保存权限数据")
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("perms")
    @Log(description = "保存权限数据", type = LogTypeEnum.SAVE)
    public ApiResponseModel<String> savePerms(@RequestBody @Validated(MenuValidation.MenuPermsValidation.class) SysMenu sysMenu) {
        return success(sysMenuService.save(sysMenu));
    }

    @Operation(summary = "更新菜单状态")
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("updateStatus/{currentStatus}")
    @Log(description = "更新菜单状态", type = LogTypeEnum.UPDATE_STATUS)
    public ApiResponseModel<String> updateStatus(@PathVariable("currentStatus") String currentStatus, @RequestBody List<String> ids) {
        return success(sysMenuService.updateStatus(ids, currentStatus));
    }

    @Operation(summary = "删除菜单数据")
    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping
    @Log(description = "删除菜单数据", type = LogTypeEnum.DELETE)
    public ApiResponseModel<String> deleteByIds(@RequestBody @NotEmpty(message = "请选择数据") List<String> ids) {
        sysMenuService.deleteByIds(ids);
        return success();
    }

    @Operation(summary = "菜单下拉框选项")
    @GetMapping("option")
    public ApiResponseModel<List<SysMenu>> menuTreeOption() {
        return success(sysMenuService.menuTreeOption());
    }
}
