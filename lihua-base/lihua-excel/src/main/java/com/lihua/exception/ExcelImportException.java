package com.lihua.exception;

import com.lihua.utils.json.JsonUtils;

import java.util.Collections;
import java.util.List;

/**
 * excel 导入异常
 */
public class ExcelImportException extends RuntimeException {

    public ExcelImportException(String message) {
        super(JsonUtils.toJson(Collections.singletonList(message)));
    }

    public ExcelImportException(List<String> errMessages) {
        super(JsonUtils.toJson(errMessages));
    }
}
