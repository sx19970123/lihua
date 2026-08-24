package com.lihua.attachment.exception;


import com.lihua.common.enums.ResultCodeEnum;
import com.lihua.common.exception.BaseException;

/**
 * 附件相关异常
 */
public class AttachmentException extends BaseException {

    public AttachmentException() {
        super(ResultCodeEnum.FILE_ERROR, null);
    }

    public AttachmentException(String message) {
        super(ResultCodeEnum.FILE_ERROR, message, null);
    }

    public AttachmentException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum, null);
    }
}
