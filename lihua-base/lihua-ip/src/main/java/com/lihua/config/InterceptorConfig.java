package com.lihua.config;

import com.lihua.interceptor.RequestIpInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Resource
    private RequestIpInterceptor requestIpInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器，拦截所有路径
        registry.addInterceptor(requestIpInterceptor).addPathPatterns("/**");
    }
}
