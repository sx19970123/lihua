package com.lihua.model.dict;

import com.lihua.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
@EqualsAndHashCode(callSuper = true)
@Data
public class SysDictDataVO extends BaseEntity {
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
    private String dictTypeCode;

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

    /**
     * 状态
     */
    private String status;

    /**
     * tag 字典回显样式
     */
    private String tagStyle;

    /**
     * 子集
     */
    private List<SysDictDataVO> children;
}
