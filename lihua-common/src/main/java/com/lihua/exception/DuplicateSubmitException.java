package com.lihua.exception;

import com.lihua.enums.ResultCodeEnum;

/**
 * 重复提交异常
 */
public class DuplicateSubmitException extends RuntimeException {
    public DuplicateSubmitException() {
        super(ResultCodeEnum.DUPLICATE_SUBMIT_ERROR.getDefaultMsg());
    }
}
