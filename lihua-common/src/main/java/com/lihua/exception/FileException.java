package com.lihua.exception;

import com.lihua.enums.ResultCodeEnum;

public class FileException extends RuntimeException {

    public FileException() {}

    public FileException(String message) {
        super(message);
    }

    public FileException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getDefaultMsg());
    }
}
