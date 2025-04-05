package com.lihua.controller.monitor;

import com.lihua.model.monitor.ServerInfo;
import com.lihua.model.web.ApiResponseModel;
import com.lihua.model.web.basecontroller.ApiResponseController;
import com.lihua.service.monitor.server.MonitorServerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "服务监控")
@RestController
@RequestMapping("monitor/server")
public class MonitorServerController extends ApiResponseController {

    @Resource
    private MonitorServerService monitorServerService;

    @Operation(summary = "获取服务器运行数据")
    @GetMapping
    public ApiResponseModel<ServerInfo> serverInfo() {
        return success(monitorServerService.serverInfo());
    }
}
