package com.lihua.job;

import com.lihua.enums.ServerSentEventsEnum;
import com.lihua.model.sse.ServerSentEventsResult;
import com.lihua.utils.sse.ServerSentEventsManager;
import com.xxl.job.core.handler.annotation.XxlJob;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SseHeartbeat {

    /**
     * 保持SSE连接，定时向客户端发送数据
     * 通过定时任务定期执行
     */
    @XxlJob("keepHeartbeat")
    public void keepHeartbeat () {
        String name = ServerSentEventsEnum.SSE_HEART_BEAT.name();
        log.info("开始执行sse定时任务，任务代码：{}",name);
        ServerSentEventsManager.send(new ServerSentEventsResult<>(ServerSentEventsEnum.SSE_HEART_BEAT, name));
    }
}
