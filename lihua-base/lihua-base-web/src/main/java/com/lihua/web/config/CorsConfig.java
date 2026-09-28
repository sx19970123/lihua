package com.lihua.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * 浏览器跨域配置（唯一 CORS 源）
 */
@Configuration
public class CorsConfig {

    /**
     * Security 链 CORS 源（SecurityConfig 的 http.cors() 按名自动探测本 bean）。
     * 受保护接口的 OPTIONS 预检不携带 Authorization，若 CORS 仅在 MVC 层配置（addCorsMappings），
     * 预检会先被认证链拒绝、到不了 MVC 层 CORS 处理——预检响应缺 Access-Control-* 头，
     * 浏览器拦截真正的跨域请求（跨域直连后端的 H5 受保护接口全灭）；
     * 挂入 Security 链后预检在认证前短路返回，实际跨域请求的响应头也由该链 CorsFilter 添加。
     * Security 链覆盖全部路径（anyRequest），MVC 层无需再另配 CORS。
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 允许所有域名
        configuration.setAllowedOriginPatterns(List.of("*"));
        // 允许的 HTTP 方法
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        // 允许所有请求头
        configuration.setAllowedHeaders(List.of("*"));
        // 预检请求缓存时间（秒）
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
