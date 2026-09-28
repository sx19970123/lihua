package com.lihua.web.exception;

import com.lihua.common.enums.ResultCodeEnum;
import com.lihua.common.exception.BaseException;

/**
 * 重复提交异常（防重复提交窗口期内同会话同参数再次提交）
 */
public class DuplicateSubmitException extends BaseException {
    public DuplicateSubmitException() {
        super(ResultCodeEnum.REPEAT_SUBMIT_ERROR, null);
    }
}
