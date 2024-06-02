package com.lihua.model.security;
import lombok.Data;
import java.io.Serializable;

@Data
public class CurrentPost implements Serializable {
    /**
     * 主键
     */
    private String id;

    /**
     * 部门id
     */
    private String deptId;

    /**
     * 部门编码
     */
    private String deptCode;

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

}
