package com.lihua.model.vo;

import lombok.Data;

/**
 * 用户头像配置（前端 AvatarType JSON 串的后端载体）
 */
@Data
public class AvatarVO {

    /**
     * 头像类型（image=图片，下发时 value 已转换为可直接访问的相对链；text/icon 无附件）
     */
    private String type;

    /**
     * 头像值（image=附件对象键/访问链，text/icon=文本或图标名）
     */
    private String value;

    /**
     * 背景色（透传字段，'auto' 跟随主题色；转换时原样保留）
     */
    private String backgroundColor;
}
