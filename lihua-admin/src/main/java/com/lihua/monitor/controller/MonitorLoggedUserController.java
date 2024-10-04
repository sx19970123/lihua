package com.lihua.monitor.controller;

import com.lihua.annotation.Log;
import com.lihua.enums.LogTypeEnum;
import com.lihua.model.web.BaseController;
import com.lihua.monitor.service.MonitorLoggedUserService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("monitor/loggedUser")
public class MonitorLoggedUserController extends BaseController {

    @Resource
    private MonitorLoggedUserService monitorLoggedUserService;

    @GetMapping
    public String findList(String username, String nickname) {
        return success(monitorLoggedUserService.findList(username, nickname));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping
    @Log(description = "强退用户", type = LogTypeEnum.OTHER)
    public String forceLogout(@RequestBody List<String> cacheKeys) {
        monitorLoggedUserService.forceLogout(cacheKeys);
        return success();
    }


}
