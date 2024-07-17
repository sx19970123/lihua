package com.lihua.system.controller;

import com.lihua.enums.ServerSentEventsEnum;
import com.lihua.exception.ServiceException;
import com.lihua.model.sse.ServerSentEventsResult;
import com.lihua.model.web.BaseController;
import com.lihua.utils.security.LoginUserContext;
import com.lihua.utils.security.LoginUserManager;
import com.lihua.utils.sse.ServerSentEventsManager;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("system/sse")
public class ServerSentEventsController extends BaseController {

    /**
     * 启动 sse 连接
     */
    @GetMapping("connect/{userId}")
    public SseEmitter connect(@PathVariable("userId") String userId) {
        if (LoginUserContext.isLogin(userId)) {
            return ServerSentEventsManager.create(userId);
        }
        throw new ServiceException("Server-Sent Events 连接失败，无法获取用户信息，请登陆后重试");
    }

    /**
     * 关闭 sse 连接
     * @return
     */
    @PostMapping("close")
    public String close() {
        ServerSentEventsManager.close();
        return success();
    }

    @GetMapping("send/{data}")
    public String send(@PathVariable("data") String data) {
        ServerSentEventsResult<String> result = new ServerSentEventsResult<>(ServerSentEventsEnum.SSE_NOTICE,data);
        ServerSentEventsManager.send(LoginUserContext.getUserId(), result);
        return success();
    }
}
