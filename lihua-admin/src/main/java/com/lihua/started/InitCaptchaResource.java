package com.lihua.started;

import cloud.tianai.captcha.resource.ResourceStore;
import cloud.tianai.captcha.resource.common.model.dto.Resource;
import com.lihua.enums.CaptchaTypeEnum;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 系统启动后加载验证码图片
 * 自定义图片资源大小为 600*360 格式为.jpg
 */
@Component
@RequiredArgsConstructor
public class InitCaptchaResource {

    @jakarta.annotation.Resource
    private ResourcePatternResolver resourcePatternResolver;

    private final ResourceStore resourceStore;

    @SneakyThrows
    @PostConstruct
    public void init() {
        // 读取 classpath:captcha-images 下附件，注册到验证码 ResourceStore 中
        org.springframework.core.io.Resource[] resources = resourcePatternResolver.getResources("classpath:captcha-images/*.jpg");
        // 获取验证码类型
        List<String> captchaTypes = CaptchaTypeEnum.allValue();
        // 所有图片均注册四种类型验证方式
        for (org.springframework.core.io.Resource resource : resources) {
            String filename = resource.getFilename();
            captchaTypes.forEach(type -> resourceStore.addResource(type, new Resource("classpath", "captcha-images/" + filename)));
        }
    }
}