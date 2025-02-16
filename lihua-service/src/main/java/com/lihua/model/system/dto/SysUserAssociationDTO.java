package com.lihua.model.system.dto;

import com.lihua.model.BaseDTO;
import com.lihua.model.system.validation.UserAssociationValidation;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserAssociationDTO extends BaseDTO {

    /**
     * 业务类型编码
     */
    @NotNull(message = "请编写业务编码",groups = {UserAssociationValidation.DetachAssociationValidation.class, UserAssociationValidation.AssociationValidation.class})
    private String code;

    /**
     * 业务主键id
     */
    @NotNull(message = "请先择数据",groups = {UserAssociationValidation.AssociateAssociationValidation.class, UserAssociationValidation.AssociationValidation.class})
    private String id;

    /**
     * 用户id
     */
    @NotNull(message = "请选择用户",groups = {UserAssociationValidation.DetachAssociationValidation.class})
    private String userId;

    /**
     * 用户id集合
     */
    @NotNull(message = "请勾选用户",groups = {UserAssociationValidation.AssociateAssociationValidation.class})
    private List<String> userIdList;

}
