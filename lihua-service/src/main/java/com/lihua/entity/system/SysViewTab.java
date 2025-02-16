package com.lihua.entity.system;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 收藏/固定页面
 */
@Data
public class SysViewTab {
    /**
     * 用户id
     */
    private String userId;

    /**
     * 路由key
     */
    @NotNull(message = "菜单id能为空")
    private String menuId;

    /**
     * 是否固定
     */
    private String affix;

    /**
     * 是否收藏
     */
    private String star;
}
