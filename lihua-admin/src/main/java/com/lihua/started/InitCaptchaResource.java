package com.lihua.started;

import cloud.tianai.captcha.common.constant.CaptchaTypeConstant;
import cloud.tianai.captcha.resource.ResourceStore;
import cloud.tianai.captcha.resource.common.model.dto.Resource;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

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
        // 读取 classpath:captcha-images 下文件，注册到验证码 ResourceStore 中
        org.springframework.core.io.Resource[] resources = resourcePatternResolver.getResources("classpath:captcha-images/*.jpg");
        // 所有图片均注册四种类型验证方式
        for (org.springframework.core.io.Resource resource : resources) {
            String filename = resource.getFilename();
            // 滑块
            resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "captcha-images/" + filename));
            // 旋转
            resourceStore.addResource(CaptchaTypeConstant.ROTATE, new Resource("classpath", "captcha-images/" + filename));
            // 拼接
            resourceStore.addResource(CaptchaTypeConstant.CONCAT, new Resource("classpath", "captcha-images/" + filename));
            // 点选
            resourceStore.addResource(CaptchaTypeConstant.WORD_IMAGE_CLICK, new Resource("classpath", "captcha-images/" + filename));
        }
    }
}