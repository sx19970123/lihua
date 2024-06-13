package com.lihua.model.security;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CurrentDept implements Serializable {

    /**
     * 主键
     */
    private String id;

    /**
     * 父级主键
     */
    private String parentId;

    /**
     * 部门名称
     */
    private String name;

    /**
     * 部门编码
     */
    private String code;

    /**
     * 部门排序
     */
    private Integer sort;

    /**
     * 部门负责人
     */
    private String manager;

    /**
     * 部门联系电话
     */
    private String phoneNumber;

    /**
     * 部门邮箱
     */
    private String email;

    /**
     * 部门传真
     */
    private String fax;

    /**
     * 0 默认部门
     */
    private String defaultDept;


    /**
     * 树型结构
     */
    private List<CurrentDept> children;

}
