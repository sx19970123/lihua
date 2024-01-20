package com.lihua.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class MetaVO implements Serializable {

    // 导航标题
    private String label;

    // 鼠标悬浮描述
    private String title;

    // 导航图标
    private String icon;

    // keep-alive 缓存 默认false，设置为true 则不进行缓存
    private boolean noCache;

    // 外联地址
    private String link;

}
