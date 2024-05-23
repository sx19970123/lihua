package com.lihua.system.model;

import com.lihua.model.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserDTO extends BaseDTO {

    private String id;

    // 部门id集合
    private List<String> deptIdList;

    // 昵称
    private String nickName;

    // 用户名
    private String username;

    // 手机号码
    private String phoneNumber;

    // 用户状态
    private String status;

    // 创建开始时间
    private List<LocalDateTime> createTimeList;

}
