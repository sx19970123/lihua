package com.lihua.entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
     * 菜单id
     */
    @NotNull(message = "菜单id不能为空")
    private String menuId;

    /**
     * 是否固定
     */
    @Pattern(regexp = "^[01]$", message = "固定标记值只能为0或1")
    private String affix;

    /**
     * 是否收藏
     */
    @Pattern(regexp = "^[01]$", message = "收藏标记值只能为0或1")
    private String star;
}
