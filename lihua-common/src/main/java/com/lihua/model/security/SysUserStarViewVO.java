package com.lihua.model.security;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 收藏 view tags 表
 */
@Data
@Accessors(chain = true)
public class SysUserStarViewVO implements Serializable {
    private String label;
    private String icon;
    private String routerPathKey;
    private boolean affix;
    private boolean star;
}
