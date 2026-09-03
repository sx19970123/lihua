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

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysDictData extends BaseEntity {
    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 父级id
     */
    @NotNull(message = "请传入父级节点")
    private String parentId;

    /**
     * 字典类型id
     */
    @NotNull(message = "请输入字典类型")
    @Size(max = 60, message = "字典类型编码长度不能超过60个字符")
    private String dictTypeCode;

    /**
     * 字典标签
     */
    @NotNull(message = "请输入字典标签")
    @Size(max = 30, message = "字典标签长度不能超过30个字符")
    private String label;

    /**
     * 字典值
     */
    @NotNull(message = "请输入字典值")
    @Size(max = 30, message = "字典值长度不能超过30个字符")
    private String value;

    /**
     * 字典排序
     */
    @NotNull(message = "请输入字典排序")
    private Integer sort;

    /**
     * 备注
     */
    @Size(max = 200, message = "备注长度不能超过200个字符")
    private String remark;

    /**
     * 状态
     */
    @NotNull(message = "请选择状态")
    @Pattern(regexp = "^[01]$", message = "字典数据状态不合法")
    private String status;

    /**
     * tag 字典回显样式
     */
    @NotNull(message = "请选择回显样式")
    @Size(max = 100, message = "回显样式长度不能超过100个字符")
    private String tagStyle;

    /**
     * 子集
     */
    @TableField(exist = false)
    private List<SysDictData> children;
}
