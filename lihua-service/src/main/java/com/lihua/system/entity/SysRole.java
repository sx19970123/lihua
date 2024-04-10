package com.lihua.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lihua.model.BaseEntity;
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
    private String name;

    /**
     * 角色编码
     */
    private String code;

    /**
     * 角色状态
     */
    private String status;

    /**
     * 逻辑删除标识
     */
    private String delFlag;

    /**
     * 备注
     */
    private String remark;

    /**
     * 菜单id集合
     */
    @TableField(exist = false)
    private List<String> menuIds = new ArrayList<>();
}
