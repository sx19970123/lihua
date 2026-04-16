package com.lihua.security.model;
import lombok.Data;

import java.io.Serializable;

@Data
public class CurrentRole implements Serializable {
    /**
     * 主键
     */
    private String id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色编码
     */
    private String code;

    /**
     * 角色状态
     */
    private String status;

    /**
     * 逻辑删除标识
     */
    private String delFlag;

    /**
     * 备注
     */
    private String remark;
}
