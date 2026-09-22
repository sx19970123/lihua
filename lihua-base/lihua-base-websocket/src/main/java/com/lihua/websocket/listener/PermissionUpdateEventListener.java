package com.lihua.websocket.listener;

import com.lihua.common.model.event.PermissionUpdateEvent;
import com.lihua.websocket.enums.WebSocketMsgTypeEnum;
import com.lihua.websocket.manager.WebSocketManager;
import com.lihua.websocket.model.WebSocketResult;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 权限数据变更事件的 WS 推送消费端：PermissionUpdateUtils.markChanged 发布的进程内事件，
 * 仅在持有 WS 连接的服务（system）内生效——依赖图自然保证「有监听器的地方才有连接」。
 * 推送为红点的在线即时提示；红点事实源在 Redis 标记，不依赖推送送达。
 * AFTER_COMMIT 消费：发布方多为事务内保存链路，待事务提交后再推送，
 * 客户端收到即回拉可见已提交数据；无事务发布方（单语句自动提交）经 fallbackExecution 立即触发
 */
@Component
public class PermissionUpdateEventListener {

    @Resource
    private WebSocketManager webSocketManager;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onPermissionUpdate(PermissionUpdateEvent event) {
        webSocketManager.send(event.getUserIds(),
                new WebSocketResult<>(WebSocketMsgTypeEnum.WS_REFRESH_PERMISSION, null));
    }
}
