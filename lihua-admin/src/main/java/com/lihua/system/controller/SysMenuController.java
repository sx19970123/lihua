package com.lihua.system.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lihua.enums.ResultCodeEnum;
import com.lihua.model.web.BaseController;
import com.lihua.system.entity.SysMenu;
import com.lihua.system.entity.validation.*;
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
     * @param sysMenu
     * @return
     */
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("list")
    public String findList(@RequestBody SysMenu sysMenu) {
        return success(sysMenuService.findList(sysMenu));
    }

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    @PreAuthorize("hasRole('ROLE_admin')")
    @GetMapping("{id}")
    public String findById(@PathVariable("id") @NotNull(message = "请选择数据") String id) {
        return success(sysMenuService.findById(id));
    }

    /**
     * 保存菜单数据，根据不同菜单类型进行分别校验
     * @param sysMenu
     * @return
     */

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("directory")
    public String saveDirectory(@RequestBody @Validated(MenuValidation.MenuDirectoryValidation.class) SysMenu sysMenu) {
        return success(sysMenuService.save(sysMenu));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("page")
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
    public String saveLink(@RequestBody @Validated(MenuValidation.MenuLinkValidation.class) SysMenu sysMenu) {
        return success(sysMenuService.save(sysMenu));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("perms")
    public String savePerms(@RequestBody @Validated(MenuValidation.MenuPermsValidation.class) SysMenu sysMenu) {
        return success(sysMenuService.save(sysMenu));
    }

        /**
         * 根据id删除菜单数据
         * @param ids
         * @return
         */
    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping
    public String deleteByIds(@RequestBody @NotEmpty(message = "请选择数据") List<String> ids) {
        sysMenuService.deleteByIds(ids);
        return success();
    }

    /**
     * 菜单下拉框选项
     * @return
     */
    @GetMapping("menuOption")
    public String menuTreeOption() {
        return success(sysMenuService.menuTreeOption());
    }
}
