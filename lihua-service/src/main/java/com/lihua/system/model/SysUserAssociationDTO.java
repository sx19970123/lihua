package com.lihua.system.model;

import com.lihua.model.BaseDTO;
import com.lihua.system.model.validation.UserAssociationValidation;
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
    @NotNull(message = "请编写业务编码",groups = {UserAssociationValidation.AssociationValidation.class})
    private String code;

    /**
     * 业务主键id
     */
    @NotNull(message = "请先择数据",groups = {UserAssociationValidation.AssociationValidation.class})
    private String id;

    /**
     * 用户id
     */
    @NotNull(message = "请选择用户",groups = {UserAssociationValidation.AssociationValidation.class, UserAssociationValidation.DetachAssociationValidation.class})
    private String userId;

    /**
     * 用户id集合
     */
    @NotNull(message = "请勾选用户",groups = {UserAssociationValidation.AssociationValidation.class, UserAssociationValidation.AssociateAssociationValidation.class})
    private List<String> userIdList;

}
