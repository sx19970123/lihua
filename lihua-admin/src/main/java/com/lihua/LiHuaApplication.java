package com.lihua;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.lihua.system.mapper")
@ComponentScan("com.lihua.*")
public class LiHuaApplication {
    public static void main(String[] args) {
        SpringApplication.run(LiHuaApplication.class, args);
    }
}
