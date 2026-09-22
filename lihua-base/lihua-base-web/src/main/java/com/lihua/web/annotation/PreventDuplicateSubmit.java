package com.lihua.web.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 防重复提交
 * <p>
 * 同一会话（token 相同，匿名请求按客户端 ip）+ 同接口 + 同参数，在窗口期内只允许提交一次。
 * 窗口期内重复提交将被拒绝；窗口期过后键自动过期放行，修改参数也会生成新的幂等键、不受窗口影响。
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreventDuplicateSubmit {

    /**
     * 幂等窗口时长（秒）
     */
    int interval() default 5;

    /**
     * 摘要前排除的参数字段名（与 @Log excludeParams 同源口径）——防止敏感字段（如密码）经幂等键进入 Redis 数据面
     */
    String[] excludeParams() default {};

}
