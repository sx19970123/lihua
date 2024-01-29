package com.lihua.system.entity;
import lombok.Data;

/**
 * 才对呢
 */
@Data
public class SysStarView {
    /**
     * 用户id
     */
    private String userId;

    /**
     * 路由key
     */
    private String routerPathKey;

    /**
     * 是否固定
     */
    private String affix;

    /**
     * 是否收藏
     */
    private String star;
}
