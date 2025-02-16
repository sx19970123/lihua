package com.lihua.exception;

import com.lihua.enums.ResultCodeEnum;

/**
 * 附件相关异常
 */
public class FileException extends RuntimeException {

    public FileException() {
        super(ResultCodeEnum.FILE_ERROR.getDefaultMsg());
    }

    public FileException(String message) {
        super(message);
    }

    public FileException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getDefaultMsg());
    }
}
