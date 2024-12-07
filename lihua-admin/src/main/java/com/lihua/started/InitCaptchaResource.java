package com.lihua.started;

import cloud.tianai.captcha.common.constant.CaptchaTypeConstant;
import cloud.tianai.captcha.resource.ResourceStore;
import cloud.tianai.captcha.resource.common.model.dto.Resource;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 系统启动后加载验证码图片
 */
@Component
@RequiredArgsConstructor
public class InitCaptchaResource {

    private final ResourceStore resourceStore;

    @PostConstruct
    public void init() {
        // 自定义背景图
        // 自定义图片资源大小为 600*360 格式为jpg
        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "captcha-images/a.jpg"));
        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "captcha-images/b.jpg"));
        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "captcha-images/c.jpg"));
        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "captcha-images/d.jpg"));
        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "captcha-images/e.jpg"));
        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "captcha-images/g.jpg"));
        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "captcha-images/h.jpg"));
        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "captcha-images/i.jpg"));
        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "captcha-images/j.jpg"));
        resourceStore.addResource(CaptchaTypeConstant.ROTATE, new Resource("classpath", "captcha-images/test.jpg"));
        resourceStore.addResource(CaptchaTypeConstant.ROTATE, new Resource("classpath", "captcha-images/48.jpg"));
        resourceStore.addResource(CaptchaTypeConstant.CONCAT, new Resource("classpath", "captcha-images/48.jpg"));
        resourceStore.addResource(CaptchaTypeConstant.WORD_IMAGE_CLICK, new Resource("classpath", "captcha-images/c.jpg"));
    }
}