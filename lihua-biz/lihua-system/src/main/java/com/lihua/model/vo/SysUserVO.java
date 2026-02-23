package com.lihua.model.vo;

import com.lihua.annotation.DictType;
import com.lihua.annotation.Sensitive;
import com.lihua.converter.DictConverter;
import com.lihua.enums.DesensitizedTypeEnum;
import com.lihua.model.BaseEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.fesod.sheet.annotation.ExcelIgnoreUnannotated;
import org.apache.fesod.sheet.annotation.ExcelProperty;
import java.util.List;

/**
 * 系统用户
 */
@ExcelIgnoreUnannotated
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
    @ExcelProperty({"用户信息", "用户名"})
    private String username;

    /**
     * 密码
     */
    @NotNull(message = "密码不能为空")
    private String password;

    /**
     * 用户名称
     */
    @ExcelProperty({"用户信息", "昵称"})
    private String nickname;

    /**
     * 性别
     */
    @ExcelProperty(value = {"用户信息", "性别"}, converter = DictConverter.class)
    @DictType("user_gender")
    private String gender;

    /**
     * 用户状态
     */
    @ExcelProperty(value = {"用户信息", "状态"}, converter = DictConverter.class)
    @DictType("sys_status")
    private String status;

    /**
     * 用户注册类型
     */
    @ExcelProperty(value = {"用户信息", "注册方式"}, converter = DictConverter.class)
    @DictType("sys_user_register_type")
    private String registerType;

    /**
     * 手机号码
     */
    @Sensitive(type = DesensitizedTypeEnum.PHONE_NUMBER, ignoreRoleCodes = {})
    @ExcelProperty({"用户信息", "手机号码"})
    private String phoneNumber;

    /**
     * 邮箱
     */
    @ExcelProperty({"用户信息", "邮箱"})
    private String email;

    /**
     * 角色名称
     */
    @ExcelProperty({"用户信息", "角色名称"})
    private String roleName;

    /**
     * 备注
     */
    @ExcelProperty({"用户信息", "备注"})
    private String remark;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户应用系统主题
     */
    private String theme;

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

}
