package com.lihua.controller.app;

import com.lihua.common.model.response.ApiResponseModel;
import com.lihua.common.model.response.basecontroller.ApiResponseController;
import com.lihua.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "APP-用户相关")
@RestController
@RequestMapping("app/system/user")
public class AppSysUserController extends ApiResponseController {

    @Resource
    private SysUserService sysUserService;

    @Operation(summary = "检查用户名是否重复")
    @PostMapping("checkUserName/{username}")
    public ApiResponseModel<Boolean> checkUserName(@PathVariable("username") String username) {
        return success(sysUserService.checkUserName(username));
    }

    @Operation(summary = "检查手机号码是否重复")
    @GetMapping("checkPhoneNumber/{phoneNumber}")
    public ApiResponseModel<Boolean> checkPhoneNumber(@PathVariable("phoneNumber") String phoneNumber) {
        return success(sysUserService.checkPhoneNumber(phoneNumber));
    }

    @Operation(summary = "检查邮箱是否重复")
    @GetMapping("checkEmail/{email}")
    public ApiResponseModel<Boolean> checkEmail(@PathVariable("email") String email) {
        return success(sysUserService.checkEmail(email));
    }

}
