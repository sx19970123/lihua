package com.lihua.security.utils;

import com.lihua.cache.enums.RedisKeyPrefixEnum;
import com.lihua.cache.manager.RedisCacheManager;
import com.lihua.common.model.event.PermissionUpdateEvent;
import com.lihua.common.utils.spring.SpringUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 权限数据更新标记（前端「数据更新」红点的事实源）
 * <p>
 * 变更即置位（markChanged：Redis 置标记 + 进程内事件通知 WS 实时推送）；
 * 登录/「数据更新」重载会话时消费（clear）；getInfo 查询（hasChanged）驱动前端红点。
 * 标记按用户维度共享——任一端登录/更新即消费、其他设备红点随之熄灭
 * （其会话数据待各自更新，已接受的粗粒度）。
 */
public class PermissionUpdateUtils {

    /**
     * 标记值：仅存在性语义，无版本含义
     */
    private static final Integer FLAG = 1;

    /**
     * 权限数据变更：置标记 + 发布进程内事件（持有 WS 连接的服务内定向推送实时通知）
     */
    public static void markChanged(String userId) {
        markChanged(List.of(userId));
    }

    /**
     * 权限数据变更（批量，角色菜单重授等场景）：置标记 + 发布进程内事件
     */
    public static void markChanged(List<String> userIds) {
        List<String> distinctUserIds = userIds.stream().filter(StringUtils::hasText).distinct().toList();
        RedisCacheManager redisCacheManager = SpringUtils.getBean(RedisCacheManager.class);
        distinctUserIds.forEach(userId ->
                redisCacheManager.setCacheObject(RedisKeyPrefixEnum.PERMISSION_UPDATE_REDIS_PREFIX.getValue() + userId, FLAG));
        SpringUtils.getApplicationContext().publishEvent(new PermissionUpdateEvent(distinctUserIds));
    }

    /**
     * 是否存在未消费的变更标记（getInfo 红点判定）
     */
    public static boolean hasChanged(String userId) {
        if (!StringUtils.hasText(userId)) {
            return false;
        }
        return SpringUtils.getBean(RedisCacheManager.class)
                .getCacheObject(RedisKeyPrefixEnum.PERMISSION_UPDATE_REDIS_PREFIX.getValue() + userId, Integer.class) != null;
    }

    /**
     * 消费变更标记（红点熄灭）：登录/「数据更新」重载会话时调用
     */
    public static void clear(String userId) {
        if (!StringUtils.hasText(userId)) {
            return;
        }
        SpringUtils.getBean(RedisCacheManager.class)
                .delete(RedisKeyPrefixEnum.PERMISSION_UPDATE_REDIS_PREFIX.getValue() + userId);
    }
}
