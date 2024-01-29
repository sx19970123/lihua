package com.lihua.model.security;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 收藏 view tags 表
 */
@Data
@Accessors(chain = true)
public class SysStarViewVO implements Serializable {
    /**
     * 页面名称
     */
    private String label;

    /**
     * 图标
     */
    private String icon;

    /**
     * 路由地址key
     */
    private String routerPathKey;

    /**
     * 是否固定
     */
    private boolean affix;

    /**
     * 是否收藏
     */
    private boolean star;

    /**
     * 默认参数
     */
    private String query;

}
