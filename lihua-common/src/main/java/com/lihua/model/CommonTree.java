package com.lihua.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 通用的树形结构
 * 对应 ant design 中 a-cascader、a-tree-select、a-tree 组件数据结构
 */
@Data
public class CommonTree implements Serializable {

    /**
     * 标签
     */
    private String label;

    /**
     * 值
     */
    private String value;

    /**
     * 子节点
     */
    private List<CommonTree> children;

    /**
     * id，用于构建树形结构
     */
    private String id;

    /**
     * 父级id，用于构建树形结构
     */
    private String parentId;

    /**
     * 类型，可自定义扩展
     */
    private String type;


}
