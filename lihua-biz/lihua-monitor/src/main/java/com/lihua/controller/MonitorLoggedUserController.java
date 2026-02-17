package com.lihua.controller;

import com.lihua.annotation.Log;
import com.lihua.enums.LogTypeEnum;
import com.lihua.model.LoggedUser;
import model.web.ApiResponseModel;
import model.web.basecontroller.ApiResponseController;
import model.web.response.ApiResponse;
import com.lihua.service.MonitorLoggedUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "登录用户信息")
@RestController
@RequestMapping("monitor/loggedUser")
public class MonitorLoggedUserController extends ApiResponseController {

    @Resource
    private MonitorLoggedUserService monitorLoggedUserService;

    @Operation(summary = "查询登录用户列表")
    @GetMapping
    public ApiResponseModel<List<LoggedUser>> queryList(String username, String nickname, String clientType) {
        return ApiResponse.success(monitorLoggedUserService.queryList(username, nickname, clientType));
    }

    @Operation(summary = "强退用户")
    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping
    @Log(description = "强退用户", type = LogTypeEnum.OTHER)
    public ApiResponseModel<String> forceLogout(@RequestBody List<String> cacheKeys) {
        monitorLoggedUserService.forceLogout(cacheKeys);
        return ApiResponse.success();
    }


}
