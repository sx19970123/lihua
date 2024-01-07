package com.lihua.config;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * spring boot 跨域配置
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Resource
    private LihuaConfig lihuaConfig;

    /**
     * 跨域配置
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                // 是否允许cookie
                .allowCredentials(true)
                // 允许的请求方式
                .allowedMethods("GET","POST","PUT","DELETE")
                .allowedHeaders("*")
                // 允许的跨域时间
                .maxAge(lihuaConfig.getCorsMaxAge());
    }
}
