package com.lihua.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SysMenuVO implements Serializable {

    /**
     * 主键
     */
    private String id;

    /**
     * 父级菜单id
     */
    private String parentId;

    /**
     * 菜单名称
     */
    private String label;

    /**
     * 菜单唯一标识
     */
    private String key;

    /**
     * 鼠标悬浮内容展示
     */
    private String title;

    /**
     * 菜单类型 group、divider等
     */
    private String type;

    /**
     * 路由地址
     */
    private String routerPath;

    /**
     * 组建路径
     */
    private String componentPath;

    /**
     * 菜单上禁用状态
     */
    private String disabled;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 权限字符串
     */
    private String perms;

    /**
     * 子菜单
     */
    private List<SysMenuVO> children;
}
