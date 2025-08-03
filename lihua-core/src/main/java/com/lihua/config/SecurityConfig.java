package com.lihua.config;

import com.lihua.filter.JwtAuthenticationTokenFilter;
import com.lihua.handle.AccessDeniedHandlerImpl;
import com.lihua.handle.AuthenticationEntryPointImpl;
import com.lihua.handle.LogoutSuccessHandlerImpl;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity()
public class SecurityConfig {

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Resource
    private AccessDeniedHandlerImpl accessDeniedHandler;

    @Resource
    private LogoutSuccessHandlerImpl logoutSuccessHandler;

    @Resource
    private AuthenticationEntryPointImpl authenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // 配置拦截请求
        http.authorizeHttpRequests(authorizeHttpRequestsCustomizer -> authorizeHttpRequestsCustomizer
                // 对于异步分发权限放开（涉及附件下载返回 ResponseEntity<StreamingResponseBody> 的情况）
                .dispatcherTypeMatchers(DispatcherType.ASYNC).permitAll()
                .requestMatchers(
                        "/captcha/**",                         // 验证码
                        "/system/login",                                // 登录
                        "/system/publicKey/**",                         // 获取公钥
                        "/system/sse/connect/**",                       // 连接sse
                        "/system/attachment/storage/download/**",       // 附件下载
                        "system/setting/GrayModelSetting",              // 灰色模式设置
                        "system/setting/SignInSetting",                 // 是否开启用户注册
                        "/system/checkUserName/**",                     // 检查用户名
                        "/system/register/**",                          // 注册
                        "/druid/**",                                    // druid数据库监控
                        "/doc.html",                                    // knife4文档
                        "/webjars/**",                                  // knife4文档
                        "/v3/api-docs/**" ,                              // knife4文档
                        "/system/user/page"                               // knife4文档
                ).permitAll()
                .anyRequest().authenticated());

        // 关闭csrf拦截
        http.csrf(AbstractHttpConfigurer::disable);

        // 允许通过iframe访问
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        // 基于前后端分离token 认证 无需session
        http.sessionManagement(sessionManagementCustomizer -> sessionManagementCustomizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 添加 jwt token 验证过滤器
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // 添加退出登陆处理器
        http.logout(logoutCustomizer -> logoutCustomizer
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandler));

        http.exceptionHandling(exceptionHandlingCustomizer -> exceptionHandlingCustomizer
                // 未认证用户访问资源/认证信息过期失效处理器
                .authenticationEntryPoint(authenticationEntryPoint)
                // 权限不足处理器
                .accessDeniedHandler(accessDeniedHandler)

        );

        return http.build();
    }

    /**
     * 全局抛出 AuthenticationManager 用于用户信息验证
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return new ProviderManager(daoAuthenticationProvider);
    }

    /**
     * 程序启动后修改认证信息上下文存储策略，支持子线程中获取认证信息
     */
    @PostConstruct
    public void setStrategyName() {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }
}
