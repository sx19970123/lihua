package com.lihua.security.config;

import com.lihua.security.filter.JwtAuthenticationTokenFilter;
import com.lihua.security.handler.LogoutSuccessHandlerImpl;
import com.lihua.security.handler.SecurityAccessDeniedHandler;
import com.lihua.security.handler.SecurityAuthenticationEntryPoint;
import jakarta.annotation.Resource;
import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Resource
    private LogoutSuccessHandlerImpl logoutSuccessHandler;

    @Resource
    private SecurityAccessDeniedHandler securityAccessDeniedHandler;

    @Resource
    private SecurityAuthenticationEntryPoint securityAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {

        // 配置拦截请求
        http.authorizeHttpRequests(authorizeHttpRequestsCustomizer -> authorizeHttpRequestsCustomizer
                // 对于异步分发权限放开（涉及附件下载返回 ResponseEntity<StreamingResponseBody> 的情况）
                .dispatcherTypeMatchers(DispatcherType.ASYNC).permitAll()
                // 后台接口配置
                .requestMatchers(
                        "/system/auth/login",                            // 登录
                        "/system/auth/register/**",                      // 注册
                        "/system/user/checkUserName/**",                 // 检查用户名
                        "/system/attachment/storage/download/**",        // 附件下载
                        "/system/setting/GrayModelSetting",              // 灰色模式设置
                        "/system/setting/base/**"                        // 基础设置
                ).permitAll()
                // app接口配置
                .requestMatchers(
                        "/app/system/auth/login",                           // 登录
                        "/app/system/auth/register/**",                     // 注册
                        "/app/system/user/checkUserName/**",                // 检查用户名
                        "/app/system/attachment/storage/download/**",       // 附件下载
                        "/app/system/setting/base/**"                       // 基础设置
                ).permitAll()
                // 系统其他接口配置
                .requestMatchers(
                        "/captcha/**",                                  // 验证码
                        "/actuator/health/**",                          // 健康探针（compose healthcheck 经主端口探活；show-details 默认 never 仅暴露整体状态；网关无该前缀路由，仅内网/本机可达）
                        "/ws-connect/**",                               // websocket建立连接
                        "/swagger-ui/**",                               // spring-doc
                        "/v3/api-docs/**",                              // spring-doc
                        "/error"                                        // 当出现404等异常时spring内部会转发到/error，需要将其放过，否则会响应401
                ).permitAll()
                .anyRequest().authenticated());

        // 关闭csrf拦截
        http.csrf(AbstractHttpConfigurer::disable);

        // CORS 接入 Security 链：按名探测 base-web CorsConfig 的 corsConfigurationSource bean（唯一 CORS 源），
        // 使受保护接口的 OPTIONS 预检在认证前短路返回
        http.cors(Customizer.withDefaults());

        // 允许通过iframe访问
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        // 基于前后端分离token 认证 无需session
        http.sessionManagement(sessionManagementCustomizer -> sessionManagementCustomizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 添加 jwt token 验证过滤器
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // 添加退出登录处理器
        http.logout(logoutCustomizer -> logoutCustomizer
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandler));

        // 添加权限/认证异常处理器
        http.exceptionHandling(exceptionHandlingCustomizer -> exceptionHandlingCustomizer
                .authenticationEntryPoint(securityAuthenticationEntryPoint)
                .accessDeniedHandler(securityAccessDeniedHandler));

        return http.build();
    }

    /**
     * 全局抛出 AuthenticationManager 用于用户信息验证
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return new ProviderManager(daoAuthenticationProvider);
    }
}
