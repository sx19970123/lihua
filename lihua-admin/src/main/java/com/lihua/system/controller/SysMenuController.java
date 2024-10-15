package com.lihua.system.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lihua.annotation.Log;
import com.lihua.enums.LogTypeEnum;
import com.lihua.enums.ResultCodeEnum;
import com.lihua.model.web.BaseController;
import com.lihua.system.entity.SysMenu;
import com.lihua.system.model.validation.MenuValidation;
import com.lihua.system.service.SysMenuService;
import com.lihua.utils.json.JsonUtils;
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
public class SysMenuController extends BaseController {

    @Resource
    private SysMenuService sysMenuService;

    /**
     * 列表查询
     */
    @PostMapping("list")
    public String findList(@RequestBody SysMenu sysMenu) {
        return success(sysMenuService.findList(sysMenu));
    }

    /**
     * 根据主键查询
     */
    @GetMapping("{id}")
    public String findById(@PathVariable("id") @NotNull(message = "请选择数据") String id) {
        return success(sysMenuService.findById(id));
    }

    /**
     * 保存菜单数据，根据不同菜单类型进行分别校验
     */

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("directory")
    @Log(description = "保存菜单数据", type = LogTypeEnum.SAVE)
    public String saveDirectory(@RequestBody @Validated(MenuValidation.MenuDirectoryValidation.class) SysMenu sysMenu) {
        return success(sysMenuService.save(sysMenu));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("page")
    @Log(description = "保存页面数据", type = LogTypeEnum.SAVE)
    public String savePage(@RequestBody @Validated(MenuValidation.MenuPageValidation.class) SysMenu sysMenu) {
        // 校验 query 是否为json参数
        if (StringUtils.hasText(sysMenu.getQuery())) {
            try {
                JsonUtils.isJson(sysMenu.getQuery());
            } catch (JsonProcessingException e) {
                return error(ResultCodeEnum.PARAMS_ERROR);
            }
        }

        return success(sysMenuService.save(sysMenu));
    }


    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("link")
    @Log(description = "保存链接数据", type = LogTypeEnum.SAVE)
    public String saveLink(@RequestBody @Validated(MenuValidation.MenuLinkValidation.class) SysMenu sysMenu) {
        return success(sysMenuService.save(sysMenu));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("perms")
    @Log(description = "保存权限数据", type = LogTypeEnum.SAVE)
    public String savePerms(@RequestBody @Validated(MenuValidation.MenuPermsValidation.class) SysMenu sysMenu) {
        return success(sysMenuService.save(sysMenu));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("updateStatus/{currentStatus}")
    @Log(description = "更新菜单状态", type = LogTypeEnum.UPDATE_STATUS)
    public String updateStatus(@PathVariable("currentStatus") String currentStatus,@RequestBody List<String> ids) {
        return success(sysMenuService.updateStatus(ids, currentStatus));
    }

    /**
     * 根据id删除菜单数据
     */
    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping
    @Log(description = "删除菜单数据", type = LogTypeEnum.DELETE)
    public String deleteByIds(@RequestBody @NotEmpty(message = "请选择数据") List<String> ids) {
        sysMenuService.deleteByIds(ids);
        return success();
    }

    /**
     * 菜单下拉框选项
     */
    @GetMapping("option")
    public String menuTreeOption() {
        return success(sysMenuService.menuTreeOption());
    }
}
