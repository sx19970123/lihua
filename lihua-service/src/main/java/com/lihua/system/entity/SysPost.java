package com.lihua.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lihua.model.BaseEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @NotNull(message = "请选择所属部门")
    private String deptId;

    /**
     * 部门编码
     */
    @NotNull(message = "请选择所属部门")
    private String deptCode;

    /**
     * 岗位名称
     */
    @NotNull(message = "请输入岗位名称")
    private String name;

    /**
     * 岗位编码
     */
    @NotNull(message = "请输入岗位编码")
    private String code;

    /**
     * 岗位排序
     */
    @NotNull(message = "请输入岗位排序")
    private Integer sort;

    /**
     * 岗位状态
     */
    @NotNull(message = "请选择状态")
    private String status;

    /**
     * 岗位负责人姓名
     */
    private String manager;

    /**
     * 岗位联系电话
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$",
            message = "请输入正确的手机号码")
    private String phoneNumber;

    /**
     * 岗位邮件
     */
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "请输入正确的邮箱地址")
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
     * 逻辑删除标志
     */
    private String delFlag;
}
