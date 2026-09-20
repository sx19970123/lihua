package com.lihua.controller.base;

import com.lihua.common.model.response.ApiResponseModel;
import com.lihua.common.model.response.basecontroller.ApiResponseController;
import com.lihua.log.annotation.Log;
import com.lihua.log.enums.LogTypeEnum;
import com.lihua.model.dto.SysProfileBasicDTO;
import com.lihua.model.dto.SysUpdatePasswordDTO;
import com.lihua.model.validation.ProfileValidation;
import com.lihua.security.model.CurrentDept;
import com.lihua.service.SysProfileService;
import com.lihua.service.SysUserDeptService;
import com.lihua.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 个人中心控制器基类：承载管理版与 App 版共有端点（对外行为双版本一致，差异仅在路由前缀与文档分组）；
 * 用户信息查询存在端侧差异（Web 版含路由与视图标签），由各子类自行声明；Web 主题仅管理版提供，
 * 登录后校验由管理版与 App 版各自提供，注销与密码验证仅 App 版提供
 */
public abstract class BaseSysProfileController extends ApiResponseController {

    @Resource
    protected SysProfileService sysProfileService;

    @Resource
    protected SysUserDeptService sysUserDeptService;

    @Resource
    protected SysUserService sysUserService;

    @Operation(summary = "保存个人信息")
    @PostMapping("basics")
    @Log(description = "保存个人信息", type = LogTypeEnum.SAVE)
    public ApiResponseModel<String> saveBasics(@RequestBody @Validated(ProfileValidation.ProfileSaveValidation.class) SysProfileBasicDTO sysProfileBasicDTO) {
        return success(sysProfileService.saveBasics(sysProfileBasicDTO));
    }

    @Operation(summary = "修改密码")
    @PostMapping("password")
    @Log(description = "修改密码", type = LogTypeEnum.SAVE, excludeParams = {"oldPassword", "newPassword", "confirmPassword"})
    public ApiResponseModel<String> updatePassword(@RequestBody @Validated SysUpdatePasswordDTO sysUpdatePasswordDTO) {
        return success(sysProfileService.updatePassword(sysUpdatePasswordDTO));
    }

    @Operation(summary = "设置默认部门")
    @PostMapping("default/{id}")
    @Log(description = "设置默认部门", type = LogTypeEnum.SAVE)
    public ApiResponseModel<CurrentDept> setDefaultDept(@PathVariable("id") String id) {
        return success(sysUserDeptService.setDefaultDept(id));
    }
}
