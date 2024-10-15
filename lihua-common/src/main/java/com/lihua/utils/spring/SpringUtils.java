package com.lihua.utils.spring;

import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;

/**
 * 非 spring 管理的类获取 bean 容器工具类
 */
@Component
public class SpringUtils implements ApplicationContextAware {
    @Getter
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@Nullable ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    // 通过clazz获取spring托管的bean
    public static <T> T getBean(Class<T> clazz) {
       return applicationContext.getBean(clazz);
    }

}
