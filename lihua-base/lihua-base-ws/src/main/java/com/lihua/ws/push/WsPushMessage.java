package com.lihua.ws.push;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * WS 推送的 Redis pub/sub 消息体：经 {@link com.lihua.cache.enums.RedisTopicEnum#WS_PUSH} 扇出
 * <p>
 * userIdList 为 null 表示全员广播；type 为 {@link com.lihua.common.enums.WebSocketMsgTypeEnum} 的 name()，
 * data 为载荷（客户端拉取兜底，推送仅是在线即时提示，消息体保持最小化）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WsPushMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 定向用户 id 集合（null = 全员广播）
     */
    private List<String> userIdList;

    /**
     * 消息类型（WebSocketMsgTypeEnum.name()）
     */
    private String type;

    /**
     * 载荷
     */
    private Object data;
}
