package com.lihua.captcha.enums;

import cloud.tianai.captcha.common.constant.CaptchaTypeConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum CaptchaTypeEnum {

    SLIDER( "滑块", CaptchaTypeConstant.SLIDER),
    CONCAT("拼接", CaptchaTypeConstant.CONCAT),
    ROTATE("旋转", CaptchaTypeConstant.ROTATE),
    WORD_IMAGE_CLICK("点选", CaptchaTypeConstant.WORD_IMAGE_CLICK);

    // 类型名称
    private final String name;

    // 类型值
    private final String value;

    // 获取验证码类型value
    public static List<String> allValue() {
        return Arrays.stream(values())
                .map(CaptchaTypeEnum::getValue)
                .toList();
    }
}
