package com.lihua.websocket.utils;

import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * WS 推送的事务提交后执行：事务内直接推送会先于数据提交到达，
 * 客户端收到消息立即回拉时读不到关联数据（如公告发布后拉取列表/未读数）
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
