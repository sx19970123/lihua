package com.lihua.monitor.controller;

import com.lihua.annotation.Log;
import com.lihua.enums.LogTypeEnum;
import com.lihua.model.web.BaseController;
import com.lihua.monitor.service.MonitorLoggedUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("monitor/loggedUser")
public class MonitorLoggedUserController extends BaseController {

    @Resource
    private MonitorLoggedUserService monitorLoggedUserService;

    @GetMapping
    public String findList(String username, String nickname) {
        return success(monitorLoggedUserService.findList(username, nickname));
    }

    @DeleteMapping("{cacheKey}")
    @Log(description = "强退用户", type = LogTypeEnum.OTHER)
    public String forceLogout(@PathVariable("cacheKey") String cacheKey) {
        return success(monitorLoggedUserService.forceLogout(cacheKey));
    }


}
