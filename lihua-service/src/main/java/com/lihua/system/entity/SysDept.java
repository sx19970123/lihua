package com.lihua.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lihua.model.BaseEntity;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "请选择上级节点")
    private String parentId;

    /**
     * 部门/岗位名称
     */
    @NotNull(message = "请输入部门/岗位名称")
    private String name;

    /**
     * 部门/岗位编码
     */
    @NotNull(message = "请输入部门/岗位编码")
    private String code;

    /**
     * 状态
     */
    @NotNull(message = "请选择状态")
    private String status;

    /**
     * 排序
     */
    @NotNull(message = "请输入排序")
    private Integer sort;

    /**
     * 负责人id
     */
    private String managerId;

    /**
     * 负责人
     */
    private String manager;

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
    @NotNull(message = "请选择类型")
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
