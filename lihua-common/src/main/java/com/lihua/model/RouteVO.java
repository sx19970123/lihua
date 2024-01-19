package com.lihua.model;

import lombok.Data;

import java.util.List;
@Data
public class RouteVO {

    // 用于生成树形结构
    private String id;

    // 用于生成树形结构
    private String parentId;

    // 组件名称（默认为组件路径首字母大写）
    private String name;

    // 路由地址
    private String path;

    // 组件路径，一级菜单为 Layout 中间菜单为 MiddleView 页面为对应组件路径
    private String component;

    // 默认的 query 参数
    private String query;

    // 自定义的元数据
    private MetaVO meta;

    // 子菜单 / 子页面
    private List<RouteVO> children;

}
