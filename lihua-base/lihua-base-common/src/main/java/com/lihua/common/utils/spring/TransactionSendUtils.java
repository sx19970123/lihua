package com.lihua.common.utils.spring;

import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * 推送/通知类动作的事务提交后执行：事务内直接推送会先于数据提交到达，
 * 客户端收到消息立即回拉时读不到关联数据（如公告发布后拉取列表/未读数）
 * <p>
 * 原 base-websocket/utils 下沉至此——投递方（base-security/lihua-system）不必依赖 base-websocket；
 * 注意不可嵌套包裹：afterCommit 回调内事务同步仍处于激活态，二次注册不会执行
 */
public class TransactionSendUtils {

    /**
     * 当前存在活动事务则挂 afterCommit 执行，否则立即执行
     */
    public static void runAfterCommit(Runnable task) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    task.run();
                }
            });
        } else {
            task.run();
        }
    }
}
