package com.lihua.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lihua.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统菜单权限表
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysMenu extends BaseEntity {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
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
     * 鼠标悬浮内容展示
     */
    private String title;

    /**
     * 菜单类型 group、divider等
     */
    private String type;

    /**
     * 菜单/页面
     */
    private String menuType;

    /**
     * 路由地址
     */
    private String routerPath;

    /**
     * 组建路径
     */
    private String componentPath;

    /**
     * 是否显示（0显示、1隐藏）
     */
    private String visible;

    /**
     * 菜单状态(0正常、1停用)
     */
    private String status;

    /**
     * 菜单上禁用状态
     */
    private String disabled;

    /**
     * 权限标识符
     */
    private String perms;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 逻辑删除标志
     */
    private String delFlag;

    /**
     * 备注
     */
    private String remark;


    /**
     * 不进行页面缓存(0缓存、1不缓存)
     */
    private String noCache;

    /**
     * 外链类型地址
     */
    private String linkPath;


}
