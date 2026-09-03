package com.lihua.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lihua.mybatis.model.BaseEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @Size(max = 30, message = "字典名称长度不能超过30个字符")
    private String name;

    /**
     * 字典类型编码
     */
    @NotNull(message = "请输入字典编码")
    @Size(max = 30, message = "字典编码长度不能超过30个字符")
    private String code;

    /**
     * 字典类型
     */
    @NotNull(message = "请选择字典类型")
    @Pattern(regexp = "^[01]$", message = "字典类型不合法")
    private String type;

    /**
     * 业务域（取值 sys_dict_business_domain 字典）
     */
    @Size(max = 30, message = "业务域长度不能超过30个字符")
    private String businessDomain;

    /**
     * 备注
     */
    @Size(max = 200, message = "备注长度不能超过200个字符")
    private String remark;

    /**
     * 字典类型状态
     */
    @NotNull(message = "请选择字典状态")
    @Pattern(regexp = "^[01]$", message = "字典状态不合法")
    private String status;
}
