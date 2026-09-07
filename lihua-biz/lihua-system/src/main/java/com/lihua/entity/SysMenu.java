package com.lihua.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lihua.model.validation.MenuValidation;
import com.lihua.mybatis.model.BaseEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @NotNull(message = "请选择上级菜单",
            groups = { MenuValidation.MenuDirectoryValidation.class, MenuValidation.MenuLinkValidation.class, MenuValidation.MenuPageValidation.class, MenuValidation.MenuPermsValidation.class })
    @Size(max = 60, message = "上级菜单id长度不能超过60个字符",
            groups = { MenuValidation.MenuDirectoryValidation.class, MenuValidation.MenuLinkValidation.class, MenuValidation.MenuPageValidation.class, MenuValidation.MenuPermsValidation.class })
    private String parentId;

    /**
     * 菜单名称
     */
    @NotNull(message = "请输入菜单名称",
            groups = { MenuValidation.MenuDirectoryValidation.class, MenuValidation.MenuLinkValidation.class, MenuValidation.MenuPageValidation.class, MenuValidation.MenuPermsValidation.class })
    @Size(max = 30, message = "菜单名称长度不能超过30个字符",
            groups = { MenuValidation.MenuDirectoryValidation.class, MenuValidation.MenuLinkValidation.class, MenuValidation.MenuPageValidation.class, MenuValidation.MenuPermsValidation.class })
    private String label;

    /**
     * 鼠标悬浮内容展示
     */
    @Size(max = 30, message = "悬浮内容长度不能超过30个字符",
            groups = { MenuValidation.MenuDirectoryValidation.class, MenuValidation.MenuLinkValidation.class, MenuValidation.MenuPageValidation.class, MenuValidation.MenuPermsValidation.class })
    private String title;

    /**
     * 菜单/页面
     */
    @NotNull(message = "请选择菜单类型",
            groups = { MenuValidation.MenuDirectoryValidation.class, MenuValidation.MenuLinkValidation.class, MenuValidation.MenuPageValidation.class, MenuValidation.MenuPermsValidation.class })
    @Size(max = 20, message = "菜单类型长度不能超过20个字符",
            groups = { MenuValidation.MenuDirectoryValidation.class, MenuValidation.MenuLinkValidation.class, MenuValidation.MenuPageValidation.class, MenuValidation.MenuPermsValidation.class })
    @Pattern(regexp = "^(directory|page|link|perms)$", message = "菜单类型不合法",
            groups = { MenuValidation.MenuDirectoryValidation.class, MenuValidation.MenuLinkValidation.class, MenuValidation.MenuPageValidation.class, MenuValidation.MenuPermsValidation.class })
    private String menuType;

    /**
     * 路由地址
     */
    @NotNull(message = "请输入路由地址",
            groups = { MenuValidation.MenuDirectoryValidation.class, MenuValidation.MenuLinkValidation.class, MenuValidation.MenuPageValidation.class })
    @Size(max = 100, message = "路由地址长度不能超过100个字符",
            groups = { MenuValidation.MenuDirectoryValidation.class, MenuValidation.MenuLinkValidation.class, MenuValidation.MenuPageValidation.class })
    @Pattern(regexp = "^/(?!.*/{2})[A-Za-z0-9_/-]*$",message = "请输入正确的路由地址",
            groups = { MenuValidation.MenuDirectoryValidation.class, MenuValidation.MenuLinkValidation.class, MenuValidation.MenuPageValidation.class })
    private String routerPath;

    /**
     * 组件路径
     */
    @NotNull(message = "请输入组件路径",
            groups =  MenuValidation.MenuPageValidation.class )
    @Size(max = 100, message = "组件路径长度不能超过100个字符",
            groups = MenuValidation.MenuPageValidation.class)
    @Pattern(regexp = "^/[A-Za-z0-9-]+(?:[./][A-Za-z0-9-]+)*\\.vue$",
            message = "请输入正确的组件路径",
            groups =  MenuValidation.MenuPageValidation.class )
    private String componentPath;

    /**
     * 是否显示（0显示、1隐藏）
     */
    @NotNull(message = "请选择菜单显示",
            groups = {MenuValidation. MenuDirectoryValidation.class, MenuValidation.MenuLinkValidation.class, MenuValidation.MenuPageValidation.class })
    @Pattern(regexp = "^[01]$", message = "菜单显示状态不合法",
            groups = { MenuValidation.MenuDirectoryValidation.class, MenuValidation.MenuLinkValidation.class, MenuValidation.MenuPageValidation.class })
    private String visible;

    /**
     * 菜单状态(0正常、1停用)
     */
    @NotNull(message = "请选择菜单状态",
            groups = { MenuValidation.MenuDirectoryValidation.class, MenuValidation.MenuLinkValidation.class, MenuValidation.MenuPageValidation.class,MenuValidation.MenuPermsValidation.class })
    @Pattern(regexp = "^[01]$", message = "菜单状态不合法",
            groups = { MenuValidation.MenuDirectoryValidation.class, MenuValidation.MenuLinkValidation.class, MenuValidation.MenuPageValidation.class, MenuValidation.MenuPermsValidation.class })
    private String status;

    /**
     * 权限标识符
     */
    @NotNull (message = "请输入权限标识符", groups = MenuValidation.MenuPermsValidation.class)
    @Size(max = 50, message = "权限标识符长度不能超过50个字符", groups = MenuValidation.MenuPermsValidation.class)
    @Pattern(regexp = "^[A-Za-z0-9:]*$",
            message = "请输入正确的权限标识",
            groups = MenuValidation.MenuPermsValidation.class)
    private String perms;

    /**
     * 菜单图标
     */
    @Size(max = 100, message = "菜单图标长度不能超过100个字符",
            groups = { MenuValidation.MenuDirectoryValidation.class, MenuValidation.MenuLinkValidation.class, MenuValidation.MenuPageValidation.class, MenuValidation.MenuPermsValidation.class })
    private String icon;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过500个字符",
            groups = { MenuValidation.MenuDirectoryValidation.class, MenuValidation.MenuLinkValidation.class, MenuValidation.MenuPageValidation.class, MenuValidation.MenuPermsValidation.class })
    private String remark;


    /**
     * 页面缓存(0缓存、1不缓存)
     */
    @NotNull(message = "请选择是否缓存",
            groups = MenuValidation.MenuPageValidation.class)
    @Pattern(regexp = "^[01]$", message = "缓存设置不合法",
            groups = MenuValidation.MenuPageValidation.class)
    private String cache;

    /**
     * 外链类型地址
     */
    @NotNull(message = "请输入外链地址", groups = MenuValidation.MenuLinkValidation.class)
    @Size(max = 300, message = "外链地址长度不能超过300个字符", groups = MenuValidation.MenuLinkValidation.class)
    @Pattern(regexp = "^(https?|ftp):\\/\\/[^\\s\\/$.?#].[^\\s]*$",
            message = "请输入正确的链接地址",
            groups = MenuValidation.MenuLinkValidation.class)
    private String linkPath;

    /**
     * 链接打开方式
     */
    @NotNull(message = "请选择链接打开方式" ,
            groups = MenuValidation.MenuLinkValidation.class)
    @Size(max = 10, message = "链接打开方式长度不能超过10个字符", groups = MenuValidation.MenuLinkValidation.class)
    private String linkOpenType;

    /**
     * 排序
     */
    @NotNull(message = "请输入显示顺序",
            groups = { MenuValidation.MenuDirectoryValidation.class, MenuValidation.MenuLinkValidation.class, MenuValidation.MenuPageValidation.class,MenuValidation.MenuPermsValidation.class })
    private Integer sort;

    /**
     * 展示在viewTab
     */
    @NotNull(message = "请选择多任务栏",
            groups = {MenuValidation.MenuLinkValidation.class, MenuValidation.MenuPageValidation.class})
    @Pattern(regexp = "^[01]$", message = "多任务栏设置不合法",
            groups = { MenuValidation.MenuLinkValidation.class, MenuValidation.MenuPageValidation.class })
    private String viewTab;

    /**
     * 路由
     */
    @Size(max = 500, message = "路由参数长度不能超过500个字符",
            groups = MenuValidation.MenuPageValidation.class)
    private String query;

    /**
     * 子元素
     */
    @TableField(exist = false)
    private List<SysMenu> children;

}
