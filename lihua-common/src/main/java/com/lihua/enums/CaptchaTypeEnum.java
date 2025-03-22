package com.lihua.enums;

import cloud.tianai.captcha.common.constant.CaptchaTypeConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum CaptchaTypeEnum {
    // 旋转和拼接不适合以人/动物等为背景的图片验证，替换为风景或其他图片时，可解除注释
    SLIDER( "滑块", CaptchaTypeConstant.SLIDER),
//    CONCAT("旋转", CaptchaTypeConstant.ROTATE),
//    ROTATE("拼接", CaptchaTypeConstant.CONCAT);
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
