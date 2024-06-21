package com.lihua.system.model;

import com.lihua.model.BaseEntity;
import com.lihua.system.entity.SysDept;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 系统用户
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserVO extends BaseEntity {
    /**
     * 主键
     */
    private String id;

    /**
     * 用户名
     */
    @NotNull(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotNull(message = "密码不能为空")
    private String password;

    /**
     * 用户名称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 性别
     */
    private String gender;

    /**
     * 用户状态
     */
    private String status;

    /**
     * 用户应用系统主题
     */
    private String theme;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 备注
     */
    private String remark;

    /**
     * 所属单位名称集合
     */
    private List<String> deptLabelList;

    /**
     * 所属单位id集合
     */
    private List<String> deptIdList;

    /**
     * 默认单位id
     */
    private String defaultDeptId;

    /**
     * 默认单位集合（用于sql接收数据）
     */
    private List<String> defaultDeptIdList;

    /**
     * 所属角色id集合
     */
    private List<String> roleIdList;

    /**
     * 所属岗位id集合
     */
    private List<String> postIdList;


    private List<SysDept> sysDeptList;
}
