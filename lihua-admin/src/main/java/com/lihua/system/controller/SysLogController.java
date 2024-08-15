package com.lihua.system.controller;

import com.lihua.annotation.Log;
import com.lihua.enums.LogTypeEnum;
import com.lihua.model.web.BaseController;
import com.lihua.system.model.dto.SysLogDTO;
import com.lihua.system.service.SysLogService;
import com.lihua.utils.file.FileDownloadUtils;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequestMapping("system/log")
@RestController
public class SysLogController extends BaseController {

    // 操作日志service
    @Resource(name = "sysOperateLogService")
    private SysLogService sysOperateLogService;

    // 登录日志service
    @Resource(name = "sysLoginLogService")
    private SysLogService sysLoginLogService;


    @GetMapping("option")
    public String getLogTypeOption() {
        // 获取日志类型下拉选项
        List<Map<String, String>> maps = Arrays
                .stream(LogTypeEnum.values())
                .filter(value -> !"LOGIN".equals(value.getCode()))
                .map(value -> Map.of("value",  value.getCode(), "label", value.getMsg()))
                .toList();
        return success(maps);
    }

    // 操作日志------------------------------------------------------------
    @PostMapping("operate/page")
    public String findOperatePage(@RequestBody SysLogDTO sysLogDTO) {
        return success(sysOperateLogService.findPage(sysLogDTO));
    }

    @GetMapping("operate/{id}")
    public String findOperateById(@PathVariable("id") String id) {
        return success(sysOperateLogService.findById(id));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping("operate")
    @Log(description = "删除操作日志", type = LogTypeEnum.DELETE)
    public String deleteOperateByIds(@RequestBody @NotEmpty(message = "请选择数据") List<String> ids) {
        sysOperateLogService.deleteByIds(ids);
        return success();
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping("operate/clear")
    @Log(description = "清空操作日志", type = LogTypeEnum.DELETE)
    public String clearOperateLog() {
        sysOperateLogService.clearLog();
        return success();
    }

    @PostMapping("operate/export")
    @Log(description = "导出操作日志", type = LogTypeEnum.EXPORT)
    public String exportOperateExcel(@RequestBody SysLogDTO sysLogDTO) {
        String path = sysOperateLogService.exportExcel(sysLogDTO);
        return success(FileDownloadUtils.addToDownloadableList(path));
    }


    // 登录日志------------------------------------------------------------

    @PostMapping("login/page")
    public String findLoginPage(@RequestBody SysLogDTO sysLogDTO) {
        return success(sysLoginLogService.findPage(sysLogDTO));
    }

    @GetMapping("login/{id}")
    public String findLoginById(@PathVariable("id") String id) {
        return success(sysLoginLogService.findById(id));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping("login")
    @Log(description = "删除登录日志", type = LogTypeEnum.DELETE)
    public String deleteLoginByIds(@RequestBody @NotEmpty(message = "请选择数据") List<String> ids) {
        sysLoginLogService.deleteByIds(ids);
        return success();
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping("login/clear")
    @Log(description = "清空登录日志", type = LogTypeEnum.DELETE)
    public String clearLoginLog() {
        sysLoginLogService.clearLog();
        return success();
    }

    @PostMapping("login/export")
    @Log(description = "导出登录日志", type = LogTypeEnum.EXPORT)
    public String exportLoginExcel(@RequestBody SysLogDTO sysLogDTO) {
        String path = sysLoginLogService.exportExcel(sysLogDTO);
        return success(FileDownloadUtils.addToDownloadableList(path));
    }

}
