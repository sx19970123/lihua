package com.lihua.controller.app;

import com.lihua.common.model.response.ApiResponseModel;
import com.lihua.controller.base.BaseSysAuthenticationController;
import com.lihua.security.manager.LoginUserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户身份验证/授权/登录数据获取/注册
 */
@Tag(name = "APP-身份验证、注册接口")
@RestController
@RequestMapping("app/system/auth")
public class AppSysAuthenticationController extends BaseSysAuthenticationController {

    /**
     * 数据更新
     */
    @Operation(summary = "重新加载当前登录用户信息")
    @PostMapping("reloadData")
    public ApiResponseModel<String> reloadData() {
        sysAuthenticationService.cacheLoginUserInfo(LoginUserContext.getLoginUser());
        return success();
    }
}
