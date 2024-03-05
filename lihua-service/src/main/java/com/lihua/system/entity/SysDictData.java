package com.lihua.system.entity;

import com.lihua.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysDictData extends BaseEntity {
    /**
     * 主键id
     */
    private String id;

    /**
     * 父级id
     */
    private String parentId;

    /**
     * 字典类型id
     */
    private String dictTypeId;

    /**
     * 字典标签
     */
    private String label;

    /**
     * 字典值
     */
    private String value;

    /**
     * 字典排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除标识
     */
    private String delFlag;
}
