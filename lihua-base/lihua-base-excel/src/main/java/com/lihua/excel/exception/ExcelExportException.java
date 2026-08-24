package com.lihua.excel.exception;

import com.lihua.common.enums.ResultCodeEnum;
import com.lihua.common.exception.BaseException;

/**
 * excel 导出异常
 */
public class ExcelExportException extends BaseException {

    public ExcelExportException() {
        super(ResultCodeEnum.EXCEL_EXPORT_ERROR, null);
    }

    public ExcelExportException(String message) {
        super(ResultCodeEnum.EXCEL_EXPORT_ERROR, message, null);
    }
}
