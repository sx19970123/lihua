package com.lihua.ws.push;

import com.lihua.cache.enums.RedisTopicEnum;
import com.lihua.cache.publisher.RedisPublisher;
import com.lihua.common.enums.WebSocketMsgTypeEnum;
import com.lihua.common.utils.json.JsonUtils;
import com.lihua.common.utils.spring.SpringUtils;

import java.util.List;

/**
 * WS 推送投递唯一入口（业务侧与 WS 连接层之间的消息边界）：业务侧经 Redis pub/sub 投递，
 * 所有订阅 {@link RedisTopicEnum#WS_PUSH} 的 WS 实例（mono 为嵌 WS 的 admin 进程、cloud 为 lihua-ws 服务）
 * 各收一次并推送本地会话——多实例部署天然扇出，投递方不持有任何连接、不依赖 lihua-websocket
 * <p>
 * 铁纪律：禁止业务侧依赖 lihua-websocket、直调 WebSocketManager 或进程内事件触达——那是单实例语义，
 * 双实例部署时另一实例的连接将收不到推送（多实例扩展的验收前提）
 * <p>
 * 「写库 + 推送」组合一律包 {@link com.lihua.common.utils.spring.TransactionSendUtils#runAfterCommit}
 * （在发布点包裹，本方法内部不包——afterCommit 回调内事务同步仍激活，嵌套注册不会执行）；
 * 推送为 fire-and-forget，无送达承诺，可靠性靠持久层 + 客户端拉取兜底
 * <p>
 * 上行消息（客户端→服务端）的业务订阅预留 {@code com.lihua.ws.receive} 包——通道统一 Redis pub/sub，
 * 勿用进程内事件（cloud 下连接层与业务不同进程，event 不跨进程）
 */
public class WebSocketPushUtils {

    /**
     * 向全部在线用户投递推送（全员广播）
     */
    public static void pushAll(WebSocketMsgTypeEnum type, Object data) {
        push(null, type, data);
    }

    /**
     * 向指定用户投递推送（userIdList 为 null 时全员广播，一般用 {@link #pushAll}；
     * 会话表无此用户的实例静默跳过，属扇出常态）
     */
    public static void push(List<String> userIdList, WebSocketMsgTypeEnum type, Object data) {
        // 消息以 JSON 字符串入 pub/sub：Redisson 全局 codec 无类型信息，POJO 直发解码退化为 Map
        SpringUtils.getBean(RedisPublisher.class)
                .send(RedisTopicEnum.WS_PUSH.getValue(), JsonUtils.toJson(new WsPushMessage(userIdList, type.name(), data)));
    }
}
