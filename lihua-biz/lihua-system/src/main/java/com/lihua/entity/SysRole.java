package com.lihua.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lihua.mybatis.model.BaseEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysRole extends BaseEntity {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 角色名称
     */
    @NotNull(message = "请输入角色名称")
    @Size(max = 60, message = "角色名称长度不能超过60个字符")
    private String name;

    /**
     * 角色编码
     */
    @NotNull(message = "请输入角色编码")
    @Size(max = 100, message = "角色编码长度不能超过100个字符")
    private String code;

    /**
     * 角色状态
     */
    @NotNull(message = "请选择角色状态")
    @Pattern(regexp = "^[01]$", message = "角色状态不合法")
    private String status;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;

    /**
     * 菜单id集合
     */
    @TableField(exist = false)
    private List<String> menuIds = new ArrayList<>();
}
