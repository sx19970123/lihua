package com.lihua.system.controller;

import com.lihua.exception.ServiceException;
import com.lihua.model.web.BaseController;
import com.lihua.utils.security.LoginUserContext;
import com.lihua.utils.sse.ServerSentEventsManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("system/sse")
public class ServerSentEventsController extends BaseController {

    /**
     * 启动 sse 连接
     */
    @GetMapping("connect/{clientKey}")
    public SseEmitter connect(@PathVariable("clientKey") String clientKey) {
        String userId = clientKey.split(":")[0];
        if (LoginUserContext.isLogin(userId)) {
            return ServerSentEventsManager.create(clientKey);
        }
        throw new ServiceException("Server-Sent Events 连接失败，无法获取用户信息，请登陆后重试");
    }

    /**
     * 关闭 sse 连接
     * @return
     */
    @PostMapping("close/{clientKey}")
    public String close(@PathVariable("clientKey") String clientKey) {
        ServerSentEventsManager.close(clientKey);
        return success();
    }

}
