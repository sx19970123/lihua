package com.lihua.system.controller;

import com.lihua.model.web.ControllerResult;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
public class ImageController extends ControllerResult {

    @GetMapping(value = "/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public void getImage(HttpServletResponse response) {
        try {
            // 指定网络图片的 URL
            URL url = new URL("https://img2.baidu.com/it/u=1814561676,2470063876&fm=253&fmt=auto&app=138&f=JPEG?w=750&h=500");

            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // 读取图片数据
            InputStream inputStream = connection.getInputStream();
            byte[] imageBytes = inputStream.readAllBytes();


            // 返回 ResponseEntity<byte[]>，指定 content type
            fileSuccess(response,imageBytes);
        } catch (Exception e) {
            // 处理异常情况
            e.printStackTrace();
        }
    }
}
