package com.lihua.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import jakarta.annotation.Resource;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4 接口文档配置类
 */
@Configuration
public class Knife4jConfig {

    @Resource
    private LihuaConfig lihuaConfig;

    /**
     * 接口描述信息
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("狸花猫后台管理系统接口文档")
                .description("接口分组配置com.lihua.config.Knife4jConfig，可自定义接口描述及接口分组")
                .contact(new Contact().name("Yukino"))
                .version(lihuaConfig.getVersion()));
    }

    /**
     * 接口分组配置（system）
     */
    @Bean
    public GroupedOpenApi systemApi() {
        return GroupedOpenApi.builder()
                .group("系统接口")
                .packagesToScan("com.lihua.controller.system")
                .build();
    }

    /**
     * 接口分组配置（monitor）
     */
    @Bean
    public GroupedOpenApi monitorApi() {
        return GroupedOpenApi.builder()
                .group("监控接口")
                .packagesToScan("com.lihua.controller.monitor")
                .build();
    }
}