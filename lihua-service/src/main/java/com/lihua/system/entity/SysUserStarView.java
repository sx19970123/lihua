package com.lihua.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 才对呢
 */
@Data
public class SysUserStarView {
    /**
     * 用户id
     */
    @TableId(type = IdType.ASSIGN_ID)
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
