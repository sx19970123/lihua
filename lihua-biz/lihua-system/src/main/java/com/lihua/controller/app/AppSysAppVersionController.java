package com.lihua.controller.app;

import com.lihua.common.model.response.ApiResponseModel;
import com.lihua.common.model.response.basecontroller.ApiResponseController;
import com.lihua.model.vo.SysAppVersionVO;
import com.lihua.service.SysAppVersionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * App 版本管理（App 端）：仅承载检查更新端点（匿名放行，白名单随 SecurityConfig），
 * 版本的维护端点仅管理版提供；App 独有端点不抽基类（无双版本一致的共有行为）
 */
@Tag(name = "APP-App版本管理")
@RestController
@RequestMapping("app/system/app-version")
public class AppSysAppVersionController extends ApiResponseController {

    @Resource
    private SysAppVersionService sysAppVersionService;

    @Operation(summary = "检查更新（platform：android/ios；versionCode：当前客户端版本序号，无更新时 data 为空）")
    @GetMapping("check")
    public ApiResponseModel<SysAppVersionVO> checkUpdate(
            @RequestParam("platform") String platform,
            @RequestParam("versionCode") @NotNull(message = "versionCode 不能为空") Integer versionCode) {
        return success(sysAppVersionService.checkUpdate(platform, versionCode));
    }
}
