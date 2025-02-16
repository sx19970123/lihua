package com.lihua.controller.monitor;

import com.lihua.model.web.BaseController;
import com.lihua.service.monitor.server.MonitorServerService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("monitor/server")
public class MonitorServerController extends BaseController {

    @Resource
    private MonitorServerService monitorServerService;

    @GetMapping
    public String serverInfo() {
        return success(monitorServerService.serverInfo());
    }
}
