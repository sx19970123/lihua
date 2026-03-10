package com.lihua.controller;

import com.lihua.log.annotation.Log;
import com.lihua.entity.SysMenu;
import com.lihua.log.enums.LogTypeEnum;
import com.lihua.model.validation.MenuValidation;
import com.lihua.common.model.response.ApiResponseModel;
import com.lihua.common.model.response.basecontroller.ApiResponseController;
import com.lihua.service.SysMenuService;
import com.lihua.common.utils.json.JsonUtils;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("system/menu")
@Slf4j
@Validated
public class SysMenuController extends ApiResponseController {

    @Resource
    private SysMenuService sysMenuService;

    @PostMapping("list")
    public ApiResponseModel<List<SysMenu>> queryList(@RequestBody SysMenu sysMenu) {
        return success(sysMenuService.queryList(sysMenu));
    }

    @GetMapping("{id}")
    public ApiResponseModel<SysMenu> queryById(@PathVariable("id") @NotNull(message = "请选择数据") String id) {
        return success(sysMenuService.queryById(id));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("directory")
    @Log(description = "保存菜单数据", type = LogTypeEnum.SAVE)
    public ApiResponseModel<String> saveDirectory(@RequestBody @Validated(MenuValidation.MenuDirectoryValidation.class) SysMenu sysMenu) {
        return success(sysMenuService.save(sysMenu));
    }

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

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("link")
    @Log(description = "保存链接数据", type = LogTypeEnum.SAVE)
    public ApiResponseModel<String> saveLink(@RequestBody @Validated(MenuValidation.MenuLinkValidation.class) SysMenu sysMenu) {
        return success(sysMenuService.save(sysMenu));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("perms")
    @Log(description = "保存权限数据", type = LogTypeEnum.SAVE)
    public ApiResponseModel<String> savePerms(@RequestBody @Validated(MenuValidation.MenuPermsValidation.class) SysMenu sysMenu) {
        return success(sysMenuService.save(sysMenu));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("updateStatus/{currentStatus}")
    @Log(description = "更新菜单状态", type = LogTypeEnum.UPDATE_STATUS)
    public ApiResponseModel<String> updateStatus(@PathVariable("currentStatus") String currentStatus, @RequestBody List<String> ids) {
        return success(sysMenuService.updateStatus(ids, currentStatus));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping
    @Log(description = "删除菜单数据", type = LogTypeEnum.DELETE)
    public ApiResponseModel<String> deleteByIds(@RequestBody @NotEmpty(message = "请选择数据") List<String> ids) {
        sysMenuService.deleteByIds(ids);
        return success();
    }

    @GetMapping("option")
    public ApiResponseModel<List<SysMenu>> menuTreeOption() {
        return success(sysMenuService.menuTreeOption());
    }
}
