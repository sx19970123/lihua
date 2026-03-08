package com.lihua.controller;

import com.lihua.annotation.Log;
import com.lihua.enums.LogTypeEnum;
import com.lihua.model.LoggedUser;
import com.lihua.model.response.ApiResponseModel;
import com.lihua.model.response.basecontroller.ApiResponseController;
import com.lihua.model.response.response.ApiResponse;
import com.lihua.service.MonitorLoggedUserService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("monitor/loggedUser")
public class MonitorLoggedUserController extends ApiResponseController {

    @Resource
    private MonitorLoggedUserService monitorLoggedUserService;

    @GetMapping
    public ApiResponseModel<List<LoggedUser>> queryList(String username, String nickname, String clientType) {
        return ApiResponse.success(monitorLoggedUserService.queryList(username, nickname, clientType));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping
    @Log(description = "强退用户", type = LogTypeEnum.OTHER)
    public ApiResponseModel<String> forceLogout(@RequestBody List<String> cacheKeys) {
        monitorLoggedUserService.forceLogout(cacheKeys);
        return ApiResponse.success();
    }


}
