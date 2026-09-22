package com.lihua.websocket.listener;

import com.lihua.common.model.event.PermissionUpdateEvent;
import com.lihua.websocket.enums.WebSocketMsgTypeEnum;
import com.lihua.websocket.manager.WebSocketManager;
import com.lihua.websocket.model.WebSocketResult;
import jakarta.annotation.Resource;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 权限数据变更事件的 WS 推送消费端：PermissionUpdateUtils.markChanged 发布的进程内事件，
 * 仅在持有 WS 连接的服务（system）内生效——依赖图自然保证「有监听器的地方才有连接」。
 * 推送为红点的在线即时提示；红点事实源在 Redis 标记，不依赖推送送达
 */
@Component
public class PermissionUpdateEventListener {

    @Resource
    private WebSocketManager webSocketManager;

    @EventListener
    public void onPermissionUpdate(PermissionUpdateEvent event) {
        webSocketManager.send(event.getUserIds(),
                new WebSocketResult<>(WebSocketMsgTypeEnum.WS_REFRESH_PERMISSION, null));
    }
}
