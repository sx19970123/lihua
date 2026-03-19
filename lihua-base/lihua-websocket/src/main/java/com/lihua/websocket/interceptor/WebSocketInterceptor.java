package com.lihua.websocket.interceptor;

import com.lihua.redis.cache.RedisCache;
import com.lihua.redis.enums.RedisKeyPrefixEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Slf4j
@Component
public class WebSocketInterceptor implements HandshakeInterceptor {

    @Resource
    private RedisCache redisCache;

    /**
     * 握手前处理
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        MultiValueMap<String, String> params = UriComponentsBuilder.fromUri(request.getURI()).build().getQueryParams();

        if (params.isEmpty() || !params.containsKey("token") || !params.containsKey("clientId") || !params.containsKey("clientType")) {
            log.error("webSocket握手失败，参数缺失");
            return false;
        }

        // 从参数中解析拿到token
        String key = RedisKeyPrefixEnum.ONCE_TOKEN_REDIS_PREFIX.getValue() + params.getFirst("token");
        String userId = redisCache.getCacheObject(key, String.class);

        if (!StringUtils.hasText(userId)) {
            log.error("webSocket握手失败，不存在的token");
            return false;
        }

        // 删除一次性token
        redisCache.delete(key);

        // 从参数拿到用户id、客户端id、客户端类型
        attributes.put("userId", userId);
        attributes.put("clientId", params.getFirst("clientId"));
        attributes.put("clientType", params.getFirst("clientType"));

        log.info("WebSocket握手完成");
        return true;
    }

    /**
     * 握手后处理
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
