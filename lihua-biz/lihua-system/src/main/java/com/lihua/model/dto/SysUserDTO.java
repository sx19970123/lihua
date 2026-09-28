package com.lihua.model.dto;

import com.lihua.mybatis.model.BaseDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserDTO extends BaseDTO {

    // 主键 id
    private String id;

    // 部门id集合
    private List<String> deptIdList;

    // 角色id集合
    private List<String> roleIdList;

    // 岗位id集合
    private List<String> postIdList;

    // 昵称
    @NotNull(message = "请输入昵称")
    @Size(max = 20, message = "昵称长度不能超过20个字符")
    private String nickname;

    // 用户名
    @NotNull(message = "请输入用户名")
    @Pattern(regexp = "^[a-zA-Z0-9@.]+$", message = "用户名只允许大小写英文、数字、@、.")
    @Size(max = 30, message = "用户名长度不能超过30个字符")
    private String username;

    // 密码
    private String password;

    // 性别
    @Pattern(regexp = "^[012]$", message = "性别不合法")
    private String gender;

    // 手机号码
    @Pattern(regexp = "^(|1[3-9]\\d{9})$",
            message = "请输入正确的手机号码")
    private String phoneNumber;

    // 用户状态
    @Pattern(regexp = "^[01]$", message = "用户状态不合法")
    private String status;

    // 电子邮箱
    @Pattern(regexp = "^(|[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$",
            message = "请输入正确的邮箱地址")
    private String email;

    // 备注
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;

    // 默认部门
    private String defaultDeptId;

    // 创建开始时间
    private List<LocalDate> createTimeList;
}
