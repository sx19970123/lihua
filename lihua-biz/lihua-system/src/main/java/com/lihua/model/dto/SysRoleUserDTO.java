package com.lihua.model.dto;

import com.lihua.mybatis.model.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysRoleUserDTO extends BaseDTO {

    // 用户昵称
    private String nickname;

    // 用户名
    private String username;

    // 用户状态
    private String status;

    // 授权状态（1 已授权 / 0 未授权 / null 全部）
    private String authorized;

    // 部门id集合
    private List<String> deptIdList;
}
