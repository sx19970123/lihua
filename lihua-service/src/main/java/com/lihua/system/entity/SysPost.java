package com.lihua.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lihua.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class SysPost extends BaseEntity {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 部门主键
     */
    private String deptId;

    /**
     * 部门编码
     */
    private String deptCode;

    /**
     * 岗位名称
     */
    private String name;

    /**
     * 岗位编码
     */
    private String code;

    /**
     * 岗位排序
     */
    private Integer sort;

    /**
     * 岗位状态
     */
    private String status;

    /**
     * 岗位负责人主键
     */
    private String managerId;

    /**
     * 岗位负责人姓名
     */
    private String manager;

    /**
     * 岗位联系电话
     */
    private String phoneNumber;

    /**
     * 岗位邮件
     */
    private String email;

    /**
     * 岗位传真号码
     */
    private String fax;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除标识
     */
    private String delFlag;
}
