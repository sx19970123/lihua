package com.lihua.system.controller;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.lihua.annotation.Log;
import com.lihua.enums.LogTypeEnum;
import com.lihua.enums.ResultCodeEnum;
import com.lihua.model.security.AuthInfo;
import com.lihua.model.security.CurrentDept;
import com.lihua.model.security.CurrentUser;
import com.lihua.model.security.LoginUser;
import com.lihua.model.web.BaseController;
import com.lihua.system.model.dto.SysRegisterDTO;
import com.lihua.system.service.SysAuthenticationService;
import com.lihua.system.service.SysSettingService;
import com.lihua.utils.security.LoginUserContext;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("system")
public class SysAuthenticationController extends BaseController {

    @Resource
    private SysAuthenticationService sysAuthenticationService;

    @Resource
    private CaptchaService captchaService;

    @Resource
    private SysSettingService sysSettingService;

    /**
     * 用户登录
     * @param currentUser
     * @param captchaVerification
     * @return
     */
    @PostMapping("login")
    @Log(description = "用户登录", type = LogTypeEnum.LOGIN, excludeParams = {"password"}, recordResult = false)
    public String login(@RequestBody @Valid CurrentUser currentUser, String captchaVerification) {
        // 开启验证码情况下进行验证
        if (sysSettingService.enableCaptcha()) {
            CaptchaVO captchaVO = new CaptchaVO();
            captchaVO.setCaptchaVerification(captchaVerification);
            ResponseModel verificationModel = captchaService.verification(captchaVO);
            if (!verificationModel.isSuccess()) {
                return error(ResultCodeEnum.ERROR, verificationModel.getRepMsg());
            }
        }

        // 调用登录返回token和配置（如果需要的话）
        Map<String, String> login = sysAuthenticationService.login(currentUser);

        return success(ResultCodeEnum.SUCCESS, login.get("setting"), login.get("token"));
    }

    /**
     * 从 SecurityContextHolder 中获取用户信息返回
     * @return
     */
    @GetMapping("info")
    public String getUserInfo() {
        LoginUser loginUser = LoginUserContext.getLoginUser();
        loginUser.getUser().setPassword(null);

        // 前端 store 用户数据
        AuthInfo authInfo = new AuthInfo();
        authInfo.setUserInfo(loginUser.getUser() != null ? loginUser.getUser() : new CurrentUser());
        authInfo.setDepts(loginUser.getDeptTree());
        authInfo.setPosts(loginUser.getPostList());
        authInfo.setRoles(loginUser.getRoleList());
        authInfo.setPermissions(loginUser.getPermissionList().stream().map(GrantedAuthority::getAuthority).filter(item -> !item.startsWith("ROLE_")).toList());
        authInfo.setRouters(loginUser.getRouterList());
        authInfo.setViewTabs(loginUser.getViewTabList());
        authInfo.setDefaultDept(LoginUserContext.getDefaultDept() != null ? LoginUserContext.getDefaultDept() : new CurrentDept());
        return success(authInfo);
    }

    /**
     * 数据更新
     * @return
     */
    @PostMapping("reloadData")
    public String reloadData() {
        sysAuthenticationService.cacheUserLoginDetails(LoginUserContext.getLoginUser());
        return success();
    }

    /**
     * 检查用户名是否重复
     * @param username
     * @return
     */
    @PostMapping("checkUserName/{username}")
    public String checkUserName(@PathVariable("username") String username) {
        return success(sysAuthenticationService.checkUserName(username));
    }

    @PostMapping("register")
    @Log(description = "用户注册", type = LogTypeEnum.REGISTER, excludeParams = {"password", "confirmPassword"}, recordResult = false)
    public String register(@RequestBody @Valid SysRegisterDTO sysRegisterDTO, String captchaVerification) {
        // 校验两次密码输入是否相同
        if (!sysRegisterDTO.getPassword().equals(sysRegisterDTO.getConfirmPassword())) {
            return error(ResultCodeEnum.ERROR, "两次输入的密码不一致");
        }
        // 校验验证码
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaVerification(captchaVerification);
        ResponseModel verificationModel = captchaService.verification(captchaVO);
        if (!verificationModel.isSuccess()) {
            return error(ResultCodeEnum.ERROR, verificationModel.getRepMsg());
        }

        // 注册
        return success(sysAuthenticationService.register(sysRegisterDTO.getUsername(), sysRegisterDTO.getPassword()));
    }
}
