package com.lihua.controller.system;

import com.lihua.exception.ServiceException;
import com.lihua.model.web.ApiResponseModel;
import com.lihua.model.web.response.ApiResponse;
import com.lihua.utils.security.LoginUserContext;
import com.lihua.utils.sse.ServerSentEventsManager;
import com.lihua.utils.web.WebUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "SSE")
@Slf4j
@RestController
@RequestMapping("system/sse")
public class ServerSentEventsController {

    /**
     * 启动 sse 连接
     */
    @Operation(summary = "启动sse连接")
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
    @Operation(summary = "关闭sse连接")
    @PostMapping("close/{clientKey}")
    public ApiResponseModel<String> close(@PathVariable("clientKey") String clientKey) {
        ServerSentEventsManager.close(clientKey);
        return ApiResponse.success();
    }

}
