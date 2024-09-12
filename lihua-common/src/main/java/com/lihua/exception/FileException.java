package com.lihua.exception;

import com.lihua.enums.ResultCodeEnum;

/**
 * 文件相关异常
 */
public class FileException extends RuntimeException {

    public FileException() {}

    public FileException(String message) {
        super(message);
    }

    public FileException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getDefaultMsg());
    }
}
