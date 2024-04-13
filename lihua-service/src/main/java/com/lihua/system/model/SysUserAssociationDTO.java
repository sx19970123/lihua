package com.lihua.system.model;

import com.lihua.model.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserAssociationDTO extends BaseDTO {

    /**
     * 业务类型编码
     */
    private String code;

    /**
     * 业务主键id
     */
    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户id集合
     */
    private List<String> userIdList;

}
