package com.lihua.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.annotation.Log;
import com.lihua.enums.LogTypeEnum;
import com.lihua.enums.ResultCodeEnum;
import com.lihua.model.validation.MaxPageSizeLimit;
import com.lihua.model.web.BaseController;
import com.lihua.model.system.dto.ResetPasswordDTO;
import com.lihua.model.system.dto.SysUserDTO;
import com.lihua.model.system.vo.SysUserVO;
import com.lihua.service.system.user.SysUserService;
import com.lihua.utils.excel.ExcelUtils;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import lombok.SneakyThrows;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("system/user")
@Validated
public class SysUserController extends BaseController {

    @Resource
    private SysUserService sysUserService;

    @PostMapping("page")
    public String queryPage(@RequestBody @Validated(MaxPageSizeLimit.class) SysUserDTO sysUserDTO) {
        return success(sysUserService.queryPage(sysUserDTO));
    }

    @GetMapping("{id}")
    public String queryById(@PathVariable("id") String id) {
        return success(sysUserService.queryById(id));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping
    @Log(description = "保存用户数据", type = LogTypeEnum.SAVE, excludeParams = {"password","passwordRequestKey"})
    public String save(@RequestBody @Validated SysUserDTO sysUserDTO) {
        if (!StringUtils.hasText(sysUserDTO.getId()) && !StringUtils.hasText(sysUserDTO.getPassword())) {
            return error(ResultCodeEnum.PARAMS_MISSING, "请输入密码");
        }
        return success(sysUserService.save(sysUserDTO));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("updateStatus/{id}/{currentStatus}")
    @Log(description = "更新用户状态", type = LogTypeEnum.UPDATE_STATUS)
    public String updateStatus(@PathVariable("id") String id,@PathVariable("currentStatus") String currentStatus) {
        return success(sysUserService.updateStatus(id, currentStatus));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping
    @Log(description = "删除用户数据", type = LogTypeEnum.DELETE)
    public String deleteByIds(@RequestBody @NotEmpty(message = "请选择数据") List<String> ids) {
        sysUserService.deleteByIds(ids);
        return success();
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("resetPassword")
    @Log(description = "重置密码", type = LogTypeEnum.SAVE, excludeParams = {"password", "passwordRequestKey"})
    public String resetPassword(@RequestBody @Validated ResetPasswordDTO resetPasswordDTO) {
        return success(sysUserService.resetPassword(resetPasswordDTO));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("export")
    @Log(description = "批量导出用户信息", type = LogTypeEnum.EXPORT)
    public String exportExcel(@RequestBody SysUserDTO sysUserDTO) {
        String path = sysUserService.exportExcel(sysUserDTO);
        return success(path);
    }

    @GetMapping("option/{deptId}")
    public String userOption(@PathVariable("deptId") String deptId) {
        return success(sysUserService.userOption(deptId));
    }

    @PostMapping("option")
    public String userOption(@RequestBody @NotEmpty(message = "集合不能为空") List<String> userIds) {
        return success(sysUserService.userOption(userIds));
    }

    @SneakyThrows
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("import")
    @Log(description = "批量导入用户信息", type = LogTypeEnum.IMPORT)
    public String importExcel(@RequestParam("file") MultipartFile file) {
        List<SysUserVO> sysUserVOS = ExcelUtils.importExport(file, SysUserVO.class, 1);
        return success(sysUserService.importExcel(sysUserVOS));
    }

}
