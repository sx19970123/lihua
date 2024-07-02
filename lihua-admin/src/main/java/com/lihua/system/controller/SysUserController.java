package com.lihua.system.controller;

import com.lihua.enums.ResultCodeEnum;
import com.lihua.model.web.BaseController;
import com.lihua.system.model.dto.SysUserDTO;
import com.lihua.system.model.vo.SysUserVO;
import com.lihua.system.service.SysUserService;
import com.lihua.utils.excel.ExcelUtils;
import com.lihua.utils.file.FileDownloadUtils;
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
public class SysUserController extends BaseController {

    @Resource
    private SysUserService sysUserService;

    @PostMapping("page")
    public String findPage(@RequestBody SysUserDTO sysUserDTO) {
        return success(sysUserService.findPage(sysUserDTO));
    }

    @GetMapping("{id}")
    public String findById(@PathVariable("id") String id) {
        return success(sysUserService.findById(id));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping
    public String save(@RequestBody @Validated SysUserDTO sysUserDTO) {
        if (!StringUtils.hasText(sysUserDTO.getId()) && !StringUtils.hasText(sysUserDTO.getPassword())) {
            return error(ResultCodeEnum.PARAMS_MISSING, "请输入密码");
        }
        return success(sysUserService.save(sysUserDTO));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("updateStatus/{id}/{currentStatus}")
    public String updateStatus(@PathVariable("id") String id,@PathVariable("currentStatus") String currentStatus) {
        return success(sysUserService.updateStatus(id, currentStatus));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping
    public String deleteByIds(@RequestBody @NotEmpty(message = "请选择数据") List<String> ids) {
        sysUserService.deleteByIds(ids);
        return success();
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("export")
    public String exportExcel(@RequestBody SysUserDTO sysUserDTO) {
        String path = sysUserService.exportExcel(sysUserDTO);
        // 根据生成的excel路径生成临时token
        return success(FileDownloadUtils.addToDownloadableList(path));
    }

    @SneakyThrows
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("import")
    public String importExcel(@RequestParam("file") MultipartFile file) {
        List<SysUserVO> sysUserVOS = ExcelUtils.importExport(file.getInputStream(), SysUserVO.class);

        return success(sysUserVOS);
    }
}
