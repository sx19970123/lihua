package com.lihua.model;

import lombok.Data;

/**
 * 用户头像配置（前端 AvatarType JSON 串的后端载体；仅消费 type/value 两个字段）
 */
@Data
public class AvatarModel {

    /**
     * 头像类型（image=图片，value 为附件对象键；text/icon 无附件）
     */
    private String type;

    /**
     * 图片头像的附件对象键
     */
    private String value;
}
