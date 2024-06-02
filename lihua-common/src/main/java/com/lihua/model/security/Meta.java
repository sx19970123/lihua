package com.lihua.model.security;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class Meta implements Serializable {

    // 导航标题
    private String label;

    // 鼠标悬浮描述
    private String title;

    // 导航图标
    private String icon;

    // 外联地址
    private String link;

    // 外链打开方式
    private String linkOpenType;

    // 菜单类型
    private String menuType;

    // 在viewTab中展示
    private Boolean viewTab;

    // 菜单显示
    private Boolean visible;

    // keep-alive 缓存 默认false，设置为true 则不进行缓存
    private Boolean cache;
}
