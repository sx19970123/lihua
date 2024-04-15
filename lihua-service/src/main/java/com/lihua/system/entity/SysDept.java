package com.lihua.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lihua.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysDept extends BaseEntity {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 父级id
     */
    private String parentId;

    /**
     * 部门/岗位名称
     */
    private String name;

    /**
     * 部门/岗位编码
     */
    private String code;

    /**
     * 状态
     */
    private String status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 负责人id
     */
    private String managerId;

    /**
     * 联系电话
     */
    private String phoneNumber;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 传真
     */
    private String fax;

    /**
     * 类型
     */
    private String type;

    /**
     * 逻辑删除标志
     */
    private String delFlag;

    /**
     * 备注
     */
    private String remark;

    /**
     * 子集
     */
    @TableField(exist = false)
    private List<SysDept> children;
}
