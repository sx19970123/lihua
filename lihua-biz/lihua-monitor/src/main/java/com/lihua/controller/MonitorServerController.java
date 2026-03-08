package com.lihua.controller;

import com.lihua.model.ServerInfo;
import com.lihua.model.ApiResponseModel;
import com.lihua.model.basecontroller.ApiResponseController;
import com.lihua.service.MonitorServerService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("monitor/server")
public class MonitorServerController extends ApiResponseController {

    @Resource
    private MonitorServerService monitorServerService;

    @GetMapping
    public ApiResponseModel<ServerInfo> serverInfo() {
        return success(monitorServerService.serverInfo());
    }
}
