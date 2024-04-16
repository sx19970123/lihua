package com.lihua.system.controller;

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
import java.util.Map;

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
    public String findById(@PathVariable("id") @NotNull(message = "主键不能为空") String id) {
        return success(sysMenuService.findById(id));
    }

    /**
     * 保存菜单数据
     * @param sysMenu
     * @return
     */
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping
    public String save(@RequestBody @Validated(MenuValidation.class) SysMenu sysMenu) {
        // 通过 Validated 进行参数校验，因分组不通，需分开进行校验
        switch (sysMenu.getMenuType()) {
            case "directory" -> {
                return saveDirectory(sysMenu);
            }
            case "page" -> {
                return savePage(sysMenu);
            }
            case "link" -> {
                return saveLink(sysMenu);
            }
            case "perms" -> {
                return savePerms(sysMenu);
            }
            default -> {
                return error(ResultCodeEnum.ERROR,"未知的菜单类型");
            }
        }
    }

    /**
     * 保存目录类型菜单
     * @param sysMenu
     * @return
     */
    private String saveDirectory(@Validated(MenuDirectoryValidation.class) SysMenu sysMenu) {
        return success(sysMenuService.save(sysMenu));
    }

    /**
     * 保存页面类型菜单
     * @param sysMenu
     * @return
     */
    private String savePage(@Validated(MenuPageValidation.class) SysMenu sysMenu) {
        // 当 query 不为空时，检查 query 是否为标准的json格式
        if (StringUtils.hasText(sysMenu.getQuery())) {
            try {
                JsonUtils.isJson(sysMenu.getQuery());
            } catch (Exception e) {
                log.error("query 不为标准json字符串 {}", e.getMessage());
                return error(ResultCodeEnum.PARAMS_ERROR,"请输入正确的参数");
            }
        }

        return success(sysMenuService.save(sysMenu));
    }

    /**
     * 保存链接类型菜单
     * @param sysMenu
     * @return
     */
    private String saveLink(@Validated(MenuLinkValidation.class) SysMenu sysMenu) {
        return success(sysMenuService.save(sysMenu));
    }

    /**
     * 保存权限类型菜单
     * @param sysMenu
     * @return
     */
    private String savePerms(@Validated(MenuPermsValidation.class) SysMenu sysMenu) {
        return success(sysMenuService.save(sysMenu));
    }

    /**
     * 根据id删除菜单数据
     * @param ids
     * @return
     */
    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping
    public String deleteByIds(@RequestBody @NotEmpty(message = "主键集合不能为空") List<String> ids) {
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
