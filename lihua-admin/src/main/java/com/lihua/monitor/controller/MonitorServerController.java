package com.lihua.monitor.controller;

import com.lihua.model.web.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("monitor/server")
public class MonitorServerController extends BaseController {

    @GetMapping
    public String serverInfo() {
        return success();
    }
}
