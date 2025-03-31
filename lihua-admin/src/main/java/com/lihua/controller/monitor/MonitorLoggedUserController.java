package com.lihua.controller.monitor;

import com.lihua.annotation.Log;
import com.lihua.enums.LogTypeEnum;
import com.lihua.model.web.ResponseController;
import com.lihua.service.monitor.logged.MonitorLoggedUserService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("monitor/loggedUser")
public class MonitorLoggedUserController extends ResponseController {

    @Resource
    private MonitorLoggedUserService monitorLoggedUserService;

    @GetMapping
    public String queryList(String username, String nickname) {
        return success(monitorLoggedUserService.queryList(username, nickname));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping
    @Log(description = "强退用户", type = LogTypeEnum.OTHER)
    public String forceLogout(@RequestBody List<String> cacheKeys) {
        monitorLoggedUserService.forceLogout(cacheKeys);
        return success();
    }


}
