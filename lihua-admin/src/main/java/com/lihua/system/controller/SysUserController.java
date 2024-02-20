package com.lihua.system.controller;

import com.lihua.model.web.ControllerResult;
import com.lihua.system.entity.SysUser;
import com.lihua.system.service.SysFileService;
import com.lihua.system.service.SysUserService;
import com.lihua.utils.file.UploadFileUtils;
import com.lihua.utils.security.LoginUserContext;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("system/user")
public class SysUserController extends ControllerResult {

    @Resource
    private SysUserService sysUserService;

    /**
     * 从 SecurityContextHolder 中获取用户信息返回
     * @return
     */
    @GetMapping("info")
    public String getUserInfo() {
        return success(LoginUserContext.getLoginUser());
    }

    @PostMapping("save")
    public String save(@RequestBody SysUser sysUser) {
        return success();
    }

    @PostMapping("theme")
    public String saveTheme(@RequestBody SysUser sysUser) {
        sysUserService.saveTheme(sysUser.getTheme());
        return success();
    }

    /**
     * 保存用户头像
     * @param file 图片文件
     * @return
     */
    @PostMapping("avatar")
    public String saveAvatar(@RequestParam("avatarFile") MultipartFile file) {
        return success(UploadFileUtils.upload(file));
    }

}
