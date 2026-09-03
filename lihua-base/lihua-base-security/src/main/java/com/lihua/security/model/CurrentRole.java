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
}
