package com.lihua.annotation.sensitive;

import com.lihua.enums.DesensitizedTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Sensitive {

    /**
     * 数据脱敏类型，在 DesensitizedTypeEnum 中维护
     */
    DesensitizedTypeEnum type();

    /**
     * 忽略数据脱敏的角色编码
     */
    String[] ignoreRoleCodes() default {"ROLE_admin"};
}
