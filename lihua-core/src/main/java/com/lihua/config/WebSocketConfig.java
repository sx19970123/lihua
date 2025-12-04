package com.lihua.config;

import com.lihua.interceptor.WebSocketInterceptor;
import com.lihua.websocket.WebSocketManager;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Resource
    private WebSocketManager webSocketManager;

    @Resource
    private WebSocketInterceptor webSocketInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketManager, "/ws-connect")
                .addInterceptors(webSocketInterceptor)
                .setAllowedOriginPatterns("*");
    }
}
