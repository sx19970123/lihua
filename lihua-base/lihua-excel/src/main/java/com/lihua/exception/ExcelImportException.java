package com.lihua.exception;

import com.lihua.enums.ResultCodeEnum;
import com.lihua.utils.json.JsonUtils;

import java.util.Collections;
import java.util.List;

/**
 * excel 导入异常
 */
public class ExcelImportException extends BaseException {

    public ExcelImportException(String message) {
        super(ResultCodeEnum.EXCEL_IMPORT_ERROR, JsonUtils.toJson(Collections.singletonList(message)));
    }

    public ExcelImportException(List<String> errMessages) {
        super(ResultCodeEnum.EXCEL_IMPORT_ERROR, JsonUtils.toJson(errMessages));
    }
}
