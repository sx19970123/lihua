package com.lihua.system.model;

import com.lihua.model.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
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
    private String nickname;

    // 用户名
    private String username;

    // 密码
    private String password;

    // 性别
    private String gender;

    // 手机号码
    private String phoneNumber;

    // 用户状态
    private String status;

    // 电子邮箱
    private String email;

    // 备注
    private String remark;

    // 默认部门
    private String defaultDeptId;

    // 创建开始时间
    private List<LocalDateTime> createTimeList;

}
