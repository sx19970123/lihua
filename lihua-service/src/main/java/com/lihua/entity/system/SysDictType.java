package com.lihua.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lihua.model.BaseEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
public class SysDictType extends BaseEntity {
    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 字典类型名称
     */
    @NotNull(message = "请输入字典名称")
    private String name;

    /**
     * 字典类型编码
     */
    @NotNull(message = "请输入字典编码")
    private String code;

    /**
     * 字典类型
     */
    @NotNull(message = "请选择字典类型")
    private String type;

    /**
     * 备注
     */
    private String remark;

    /**
     * 逻辑删除标识
     */
    private String delFlag;

    /**
     * 字典类型状态
     */
    @NotNull(message = "请选择字典状态")
    private String status;
}
