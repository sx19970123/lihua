package com.lihua.system.controller;

import com.lihua.enums.LogTypeEnum;
import com.lihua.model.web.BaseController;
import com.lihua.system.model.dto.SysLogDTO;
import com.lihua.system.service.SysLogService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RequestMapping("system/log")
@RestController
public class SysLogController extends BaseController {

    // 登录日志service
    @Resource(name = "sysLoginLogService")
    private SysLogService sysLoginLogService;

    // 操作日志service
    @Resource(name = "sysOperateLogService")
    private SysLogService sysOperateLogService;


    @PostMapping("page")
    public String findPage(@RequestBody SysLogDTO sysLogDTO) {
        // 分页查询登录日志
        if (LogTypeEnum.LOGIN.getCode().equals(sysLogDTO.getTypeCode())) {
            return success(sysLoginLogService.findPage(sysLogDTO));
        }
        // 分页查询操作日志
        return success(sysOperateLogService.findPage(sysLogDTO));
    }

    @GetMapping("{typeCode}/{id}")
    public String findById(@PathVariable("typeCode") String typeCode, @PathVariable("id") String id) {
        // 查询登录日志详情
        if (LogTypeEnum.LOGIN.getCode().equals(typeCode)) {
            return success(sysLoginLogService.findById(id));
        }
        // 查询操作详情
        return success(sysOperateLogService.findById(id));
    }

    @GetMapping("option")
    public String getLogTypeOption() {
        // 获取日志类型下拉选项
        return success(LogTypeEnum.values());
    }
}
