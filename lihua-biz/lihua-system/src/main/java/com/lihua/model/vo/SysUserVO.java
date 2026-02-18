package com.lihua.model.vo;

import com.lihua.annotation.Sensitive;
import com.lihua.enums.DesensitizedTypeEnum;
import com.lihua.model.BaseEntity;
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
    @Sensitive(type = DesensitizedTypeEnum.PHONE_NUMBER, ignoreRoleCodes = {})
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
     * 角色名称
     */
    private String roleName;

    /**
     * 所属部门名称集合
     */
    private List<String> deptLabelList;

    /**
     * 所属部门编码集合
     */
    private List<String> deptCodeList;

    /**
     * 所属部门下的岗位名称
     */
    private List<String> postLabelList;

    /**
     * 所属部门id集合
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

    /**
     * 角色名称集合
     */
    private List<String> roleNameList;

    /**
     * excel 批量导入异常说明
     * 数据导入后，因异常无法入库的数据错误描述
     */
    private String importErrorMsg;

    /**
     * 用户注册类型
     */
    private String registerType;
}
