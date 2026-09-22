package com.lihua.common.model.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 权限数据变更事件：角色/菜单/用户角色变更后由 PermissionUpdateUtils.markChanged 发布（进程内 Spring Event），
 * 持有 WS 连接的服务内由 base-websocket 的监听器消费并定向推送实时通知。
 * 事件不跨服务/跨实例传播——红点判定不依赖本事件（Redis 标记为事实源），
 * 未部署监听器的服务调用 markChanged 时自动降级为仅置标记
 */
@Getter
@RequiredArgsConstructor
public class PermissionUpdateEvent implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // 受影响用户 id 集合（去重由发布方保证）
    private final List<String> userIds;

    public PermissionUpdateEvent(String userId) {
        this(List.of(userId));
    }
}
