package com.lihua.enums;

import com.lihua.common.enums.DictEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 菜单类型字典（sys_menu_type）枚举：目录 / 页面 / 外链 / 权限
 */
@Getter
@AllArgsConstructor
public enum MenuTypeEnum implements DictEnum {

    /**
     * 目录
     */
    DIRECTORY("directory"),

    /**
     * 页面
     */
    PAGE("page"),

    /**
     * 外链
     */
    LINK("link"),

    /**
     * 权限
     */
    PERMS("perms");

    private final String value;

    @Override
    public String getType() {
        return "sys_menu_type";
    }
}
