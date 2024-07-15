package com.lihua.model.web;

import lombok.Data;

@Data
public class EditorUrlFileModel {

    /**
     * 原始url
     */
    private String originalURL;

    /**
     * 上传后新url
     */
    private String url;
}
