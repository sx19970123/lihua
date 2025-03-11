package com.lihua.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

@Getter
@AllArgsConstructor
public enum DesensitizedTypeEnum {

    /**
     * 手机号码，中间四位替换
     */
    PHONE_NUMBER(str -> str.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1••••$2")),

    /**
     * 电子邮箱，显示第一个字符和@后字符
     */
    EMAIL(str -> str.replaceAll("(\\w{2})[^@]+@(\\w+)", "$1••@$2")),

    /**
     * ip地址，隐藏中间
     */
    IP(str -> str.replaceAll("(\\d+\\.)\\d+(\\.\\d+\\.\\d+)", "$1•••$2")),

    /**
     * 身份证号，显示前后四位
     */
    ID_CARD(str -> str.replaceAll("(\\d{4})\\d{10}(\\w{4})", "$1••••••••••$2")),

    /**
     * 银行卡号，显示最后四位
     */
    BANK_CARD(s -> s.replaceAll("\\d{15}(\\d{3})", "•••• •••• •••• •••• $1"));


    private final Function<String, String> function;

}
