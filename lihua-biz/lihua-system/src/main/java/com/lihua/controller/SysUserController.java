package com.lihua.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.common.enums.ResultCodeEnum;
import com.lihua.common.model.response.ApiResponseModel;
import com.lihua.common.model.response.basecontroller.ApiResponseController;
import com.lihua.entity.SysUser;
import com.lihua.excel.merge.UserMergeStrategy;
import com.lihua.excel.utils.ExcelUtils;
import com.lihua.log.annotation.Log;
import com.lihua.log.enums.LogTypeEnum;
import com.lihua.model.dto.ResetPasswordDTO;
import com.lihua.model.dto.SysUserDTO;
import com.lihua.model.vo.SysUserVO;
import com.lihua.mybatis.model.validation.MaxPageSizeLimit;
import com.lihua.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "用户管理")
@RestController
@RequestMapping("system/user")
@Validated
public class SysUserController extends ApiResponseController {

    @Resource
    private SysUserService sysUserService;

    @Operation(summary = "分页查询")
    @PostMapping("page")
    public ApiResponseModel<IPage<SysUserVO>> queryPage(@RequestBody @Validated(MaxPageSizeLimit.class) SysUserDTO sysUserDTO) {
        return success(sysUserService.queryPage(sysUserDTO));
    }

    @Operation(summary = "根据id查询详情")
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

    @Operation(summary = "更新状态")
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

    @Operation(summary = "下载模板")
    @GetMapping("exportTemplate")
    public void exportTemplate() {
        ExcelUtils.export(new ArrayList<>(), SysUser.class);
    }

    @Operation(summary = "导出用户信息")
    @PostMapping("export")
    @Log(description = "批量导出用户信息", type = LogTypeEnum.EXPORT)
    public void exportExcel(@RequestBody SysUserDTO sysUserDTO) {
        List<SysUserVO> sysUserVOS = sysUserService.exportExcel(sysUserDTO);
        ExcelUtils.export(sysUserVOS, SysUserVO.class, new UserMergeStrategy(sysUserVOS.size()));
    }

    @Operation(summary = "根据部门获取系统用户选项")
    @GetMapping("option/{deptId}")
    public ApiResponseModel<List<SysUser>> userOption(@PathVariable("deptId") String deptId) {
        return success(sysUserService.userOption(deptId));
    }

    @Operation(summary = "根据id获取用户选项")
    @PostMapping("option")
    public ApiResponseModel<List<SysUser>> userOption(@RequestBody @NotEmpty(message = "集合不能为空") List<String> userIds) {
        return success(sysUserService.userOption(userIds));
    }

    @Operation(summary = "批量导入")
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("import")
    @Log(description = "批量导入用户信息", type = LogTypeEnum.IMPORT)
    public ApiResponseModel<String> importExcel(@RequestParam("file") MultipartFile file) throws IOException {
        List<SysUser> sysUserList = ExcelUtils.excelImport(file.getInputStream(), SysUser.class);
        sysUserService.importExcel(sysUserList);
        return success();
    }

}
