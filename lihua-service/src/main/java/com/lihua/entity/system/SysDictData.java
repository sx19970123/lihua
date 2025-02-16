package com.lihua.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lihua.model.BaseEntity;
import jakarta.validation.constraints.NotNull;
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
    private String dictTypeCode;

    /**
     * 字典标签
     */
    @NotNull(message = "请输入字典标签")
    private String label;

    /**
     * 字典值
     */
    @NotNull(message = "请输入字典值")
    private String value;

    /**
     * 字典排序
     */
    @NotNull(message = "请输入字典排序")
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除标识
     */
    private String delFlag;

    /**
     * 状态
     */
    @NotNull(message = "请选择状态")
    private String status;

    /**
     * tag 字典回显样式
     */
    @NotNull(message = "请选择回显样式")
    private String tagStyle;

    /**
     * 子集
     */
    @TableField(exist = false)
    private List<SysDictData> children;
}
