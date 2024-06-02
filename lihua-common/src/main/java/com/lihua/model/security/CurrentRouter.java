package com.lihua.model.security;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
@Data
@Accessors(chain = true)
public class CurrentRouter implements Serializable {

    // 用于生成树形结构
    private String id;

    // 用于生成树形结构
    private String parentId;

    // 组件名称（默认为组件路由地址大驼峰）
    private String name;

    // 路由地址
    private String path;

    // 组件路径，一级菜单为 Layout 中间菜单为 MiddleView 页面为对应组件路径
    private String component;

    // 默认的 query 参数
    private String query;

    // 用户权限标识
    private String perms;

    // 菜单/组件页面
    private String type;

    // page类型数据从父级到子节点的路由地址拼接
    private String key;

    // 自定义的元数据
    private Meta meta;

    // 子菜单 / 子页面
    private List<CurrentRouter> children;

}
