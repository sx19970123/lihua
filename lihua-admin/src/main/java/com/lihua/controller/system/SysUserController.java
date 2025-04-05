package com.lihua.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.annotation.Log;
import com.lihua.entity.system.SysUser;
import com.lihua.enums.LogTypeEnum;
import com.lihua.enums.ResultCodeEnum;
import com.lihua.model.excel.ExcelImportResult;
import com.lihua.model.validation.MaxPageSizeLimit;
import com.lihua.model.web.ApiResponseModel;
import com.lihua.model.web.basecontroller.ApiResponseController;
import com.lihua.model.system.dto.ResetPasswordDTO;
import com.lihua.model.system.dto.SysUserDTO;
import com.lihua.model.system.vo.SysUserVO;
import com.lihua.service.system.user.SysUserService;
import com.lihua.utils.excel.ExcelUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("system/user")
@Validated
@Tag(name = "用户管理")
public class SysUserController extends ApiResponseController {

    @Resource
    private SysUserService sysUserService;

    @Operation(summary = "分页查询用户")
    @PostMapping("page")
    public ApiResponseModel<IPage<SysUserVO>> queryPage(@RequestBody @Validated(MaxPageSizeLimit.class) SysUserDTO sysUserDTO) {
        return success(sysUserService.queryPage(sysUserDTO));
    }

    @Operation(summary = "根据id查询用户")
    @GetMapping("{id}")
    public ApiResponseModel<SysUserVO> queryById(@PathVariable("id") String id) {
        return success(sysUserService.queryById(id));
    }

    @Operation(summary = "保存用户数据")
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping
    @Log(description = "保存用户数据", type = LogTypeEnum.SAVE, excludeParams = {"password","passwordRequestKey"})
    public ApiResponseModel<String> save(@RequestBody @Validated SysUserDTO sysUserDTO) {
        if (!StringUtils.hasText(sysUserDTO.getId()) && !StringUtils.hasText(sysUserDTO.getPassword())) {
            return error(ResultCodeEnum.PARAMS_MISSING, "请输入密码");
        }
        return success(sysUserService.save(sysUserDTO));
    }

    @Operation(summary = "更新用户状态")
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("updateStatus/{id}/{currentStatus}")
    @Log(description = "更新用户状态", type = LogTypeEnum.UPDATE_STATUS)
    public ApiResponseModel<String> updateStatus(@PathVariable("id") String id, @PathVariable("currentStatus") String currentStatus) {
        return success(sysUserService.updateStatus(id, currentStatus));
    }

    @Operation(summary = "删除用户数据")
    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping
    @Log(description = "删除用户数据", type = LogTypeEnum.DELETE)
    public ApiResponseModel<String> deleteByIds(@RequestBody @NotEmpty(message = "请选择数据") List<String> ids) {
        sysUserService.deleteByIds(ids);
        return success();
    }

    @Operation(summary = "重置密码")
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("resetPassword")
    @Log(description = "重置密码", type = LogTypeEnum.SAVE, excludeParams = {"password", "passwordRequestKey"})
    public ApiResponseModel<String> resetPassword(@RequestBody @Validated ResetPasswordDTO resetPasswordDTO) {
        return success(sysUserService.resetPassword(resetPasswordDTO));
    }

    @Operation(summary = "批量导出用户信息")
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("export")
    @Log(description = "批量导出用户信息", type = LogTypeEnum.EXPORT)
    public ApiResponseModel<String> exportExcel(@RequestBody SysUserDTO sysUserDTO) {
        return success(sysUserService.exportExcel(sysUserDTO));
    }

    @Operation(summary = "系统用户选项（根据部门选择）")
    @GetMapping("option/{deptId}")
    public ApiResponseModel<List<SysUser>> userOption(@PathVariable("deptId") String deptId) {
        return success(sysUserService.userOption(deptId));
    }

    @Operation(summary = "系统用户选项（根据用户id集合）")
    @PostMapping("option")
    public ApiResponseModel<List<SysUser>> userOption(@RequestBody @NotEmpty(message = "集合不能为空") List<String> userIds) {
        return success(sysUserService.userOption(userIds));
    }

    @Operation(summary = "批量导入用户信息")
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("import")
    @Log(description = "批量导入用户信息", type = LogTypeEnum.IMPORT)
    public ApiResponseModel<ExcelImportResult> importExcel(@RequestParam("file") MultipartFile file) {
        List<SysUserVO> sysUserVOS = ExcelUtils.importExport(file, SysUserVO.class, 1);
        return success(sysUserService.importExcel(sysUserVOS));
    }

}
