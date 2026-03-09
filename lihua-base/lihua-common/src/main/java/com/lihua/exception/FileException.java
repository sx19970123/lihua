package com.lihua.exception;


import com.lihua.enums.ResultCodeEnum;

/**
 * 附件相关异常
 */
public class FileException extends BaseException {

    public FileException() {
        super(ResultCodeEnum.FILE_ERROR, null);
    }

    public FileException(String message) {
        super(ResultCodeEnum.FILE_ERROR, message);
    }

    public FileException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum, null);
    }
}
