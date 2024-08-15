package com.lihua.system.controller;

import com.lihua.enums.LogTypeEnum;
import com.lihua.model.web.BaseController;
import com.lihua.system.model.dto.SysLogDTO;
import com.lihua.system.service.SysLogService;
import com.lihua.utils.file.FileDownloadUtils;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
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

    @DeleteMapping("operate")
    public String deleteOperateByIds(@RequestBody @NotEmpty(message = "请选择数据") List<String> ids) {
        sysOperateLogService.deleteByIds(ids);
        return success();
    }

    @DeleteMapping("operate/clear")
    public String clearOperateLog() {
        sysOperateLogService.clearLog();
        return success();
    }

    @PostMapping("operate/export")
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


    @DeleteMapping("login")
    public String deleteLoginByIds(@RequestBody @NotEmpty(message = "请选择数据") List<String> ids) {
        sysLoginLogService.deleteByIds(ids);
        return success();
    }

    @DeleteMapping("login/clear")
    public String clearLoginLog() {
        sysLoginLogService.clearLog();
        return success();
    }

    @PostMapping("login/export")
    public String exportLoginExcel(@RequestBody SysLogDTO sysLogDTO) {
        String path = sysLoginLogService.exportExcel(sysLogDTO);
        return success(FileDownloadUtils.addToDownloadableList(path));
    }

}
