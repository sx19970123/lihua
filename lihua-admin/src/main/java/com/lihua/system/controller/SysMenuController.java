package com.lihua.system.controller;

import com.lihua.enums.ResultCodeEnum;
import com.lihua.model.web.BaseController;
import com.lihua.system.entity.SysMenu;
import com.lihua.system.service.SysMenuService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("system/menu")
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
    public String findById(@PathVariable("id") String id) {
        if (!StringUtils.hasText(id)) {
            return error(ResultCodeEnum.PRIMARY_KEY_IS_EMPTY);
        }

        return success(sysMenuService.findById(id));
    }

    /**
     * 保存菜单数据
     * @param sysMenu
     * @return
     */
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping
    public String save(@RequestBody SysMenu sysMenu) {
        return success(sysMenuService.save(sysMenu));
    }

    /**
     * 根据id删除菜单数据
     * @param ids
     * @return
     */
    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping
    public String deleteByIds(@RequestBody List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return error(ResultCodeEnum.PRIMARY_KEY_COLLECTION_IS_EMPTY);
        }
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
