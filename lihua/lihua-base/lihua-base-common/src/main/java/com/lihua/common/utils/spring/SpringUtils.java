package com.lihua.common.utils.spring;

import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 非 spring 管理的类获取 bean 容器工具类
 */
@Component
public class SpringUtils implements ApplicationContextAware {

    @Getter
    private static ApplicationContext applicationContext;

    /**
     * 通过clazz获取spring托管的bean
     * @param clazz 获取对应bean的class信息
     */
    public static <T> T getBean(Class<T> clazz) {
       return applicationContext.getBean(clazz);
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext context) throws BeansException {
        applicationContext = context;
    }
}
