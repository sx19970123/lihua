package com.lihua.system.model;

import com.lihua.model.BaseDTO;
import com.lihua.system.enums.BusinessUserEnums;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysBusinessUserDTO extends BaseDTO {

    /**
     * 业务类型编码
     */
    private String businessCode;

    /**
     * 业务主键id
     */
    private String businessId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户id集合
     */
    private List<String> userIdList;

}
