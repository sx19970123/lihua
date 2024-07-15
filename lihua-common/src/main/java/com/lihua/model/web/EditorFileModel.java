package com.lihua.model.web;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * editor 文件上传返回实体
 */
@Data
public class EditorFileModel {

    /**
     * 上传失败的文件名称
     */
    private List<String> errFiles;

    /**
     * 上传成功的文件map集合
     * key：文件名
     * value：文件地址
     */
    private Map<String, String> succMap;
}
