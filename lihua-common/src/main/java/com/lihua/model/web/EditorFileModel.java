package com.lihua.model.web;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * editor 附件上传返回实体
 */
@Data
public class EditorFileModel {

    /**
     * 上传失败的附件名称
     */
    private List<String> errFiles;

    /**
     * 上传成功的附件map集合
     * key：附件名
     * value：附件地址
     */
    private Map<String, String> succMap;
}
