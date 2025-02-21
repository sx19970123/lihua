package com.lihua.controller.system;

import com.lihua.annotation.Log;
import com.lihua.enums.LogTypeEnum;
import com.lihua.exception.ServiceException;
import com.lihua.model.web.BaseController;
import com.lihua.utils.security.LoginUserContext;
import com.lihua.utils.sse.ServerSentEventsManager;
import com.lihua.utils.web.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequestMapping("system/sse")
public class ServerSentEventsController extends BaseController {

    /**
     * 启动 sse 连接
     */
    @GetMapping("connect/{clientKey}")
    public SseEmitter connect(@PathVariable("clientKey") String clientKey) {
        log.info("Server-Sent Events clientKey【{}】请求连接，请求IP为【{}】",clientKey, WebUtils.getIpAddress());

        String userId = clientKey.split(":")[0];
        if (LoginUserContext.isLogin(userId)) {
            return ServerSentEventsManager.create(clientKey);
        }

        throw new ServiceException("Server-Sent Events 连接失败，无法获取用户信息，请登陆后重试");
    }

    /**
     * 关闭 sse 连接
     */
    @PostMapping("close/{clientKey}")
    public String close(@PathVariable("clientKey") String clientKey) {
        ServerSentEventsManager.close(clientKey);
        return success();
    }

}
