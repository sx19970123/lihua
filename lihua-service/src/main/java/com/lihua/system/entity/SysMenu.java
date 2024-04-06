package com.lihua.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lihua.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

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
     * 页面缓存(0缓存、1不缓存)
     */
    private String cache;

    /**
     * 外链类型地址
     */
    private String linkPath;

    /**
     * 链接打开方式
     */
    private String linkOpenType;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 展示在viewTab
     */
    private String viewTab;

    /**
     * 路由
     */
    private String query;

    /**
     * 子元素
     */
    @TableField(exist = false)
    private List<SysMenu> children;

}
