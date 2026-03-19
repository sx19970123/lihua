package com.lihua.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.common.model.response.ApiResponseModel;
import com.lihua.common.model.response.basecontroller.ApiResponseController;
import com.lihua.excel.utils.ExcelUtils;
import com.lihua.log.annotation.Log;
import com.lihua.log.enums.LogTypeEnum;
import com.lihua.model.dto.SysLogDTO;
import com.lihua.model.vo.SysLogVO;
import com.lihua.mybatis.model.validation.MaxPageSizeLimit;
import com.lihua.service.SysLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Tag(name = "系统日志")
@RequestMapping("system/log")
@RestController
@Validated
public class SysLogController extends ApiResponseController {

    // 操作日志service
    @Resource(name = "sysOperateLogService")
    private SysLogService sysOperateLogService;

    // 登录日志service
    @Resource(name = "sysLoginLogService")
    private SysLogService sysLoginLogService;

    @Operation(summary = "获取日志类型选项")
    @GetMapping("option")
    public ApiResponseModel<List<Map<String, String>>> getLogTypeOption() {
        List<Map<String, String>> maps = Arrays
                .stream(LogTypeEnum.values())
                .filter(value -> !"LOGIN".equals(value.getCode()))
                .map(value -> Map.of("value",  value.getCode(), "label", value.getMsg()))
                .toList();
        return success(maps);
    }

    // 操作日志------------------------------------------------------------
    @Operation(summary = "操作日志-分页查询")
    @PostMapping("operate/page")
    public ApiResponseModel<IPage<? extends SysLogVO>> queryOperatePage(@RequestBody @Validated(MaxPageSizeLimit.class) SysLogDTO sysLogDTO) {
        return success(sysOperateLogService.queryPage(sysLogDTO));
    }

    @Operation(summary = "操作日志-根据id查询详情")
    @GetMapping("operate/{id}")
    public ApiResponseModel<SysLogVO> queryOperateById(@PathVariable("id") String id) {
        return success(sysOperateLogService.queryById(id));
    }

    @Operation(summary = "操作日志-批量删除")
    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping("operate")
    @Log(description = "删除操作日志", type = LogTypeEnum.DELETE)
    public ApiResponseModel<String> deleteOperateByIds(@RequestBody @NotEmpty(message = "请选择数据") List<String> ids) {
        sysOperateLogService.deleteByIds(ids);
        return success();
    }

    @Operation(summary = "操作日志-清空日志")
    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping("operate/clear")
    @Log(description = "清空操作日志", type = LogTypeEnum.DELETE)
    public ApiResponseModel<String> clearOperateLog() {
        sysOperateLogService.clearLog();
        return success();
    }

    @Operation(summary = "操作日志-导出日志")
    @PostMapping("operate/export")
    @Log(description = "导出操作日志", type = LogTypeEnum.EXPORT)
    public void exportOperateExcel(@RequestBody SysLogDTO sysLogDTO) {
        List<? extends SysLogVO> sysLogVOS = sysOperateLogService.exportExcel(sysLogDTO);
        ExcelUtils.export(sysLogVOS, SysLogVO.class);
    }


    // 登录日志------------------------------------------------------------

    @Operation(summary = "登录日志-分页查询")
    @PostMapping("login/page")
    public ApiResponseModel<IPage<? extends SysLogVO>> queryLoginPage(@RequestBody @Validated(MaxPageSizeLimit.class) SysLogDTO sysLogDTO) {
        return success(sysLoginLogService.queryPage(sysLogDTO));
    }

    @Operation(summary = "登录日志-根据id查询详情")
    @GetMapping("login/{id}")
    public ApiResponseModel<SysLogVO> queryLoginById(@PathVariable("id") String id) {
        return success(sysLoginLogService.queryById(id));
    }

    @Operation(summary = "登录日志-根据缓存查询日志")
    @GetMapping("login/cacheKey/{cacheKey}")
    public ApiResponseModel<SysLogVO> queryLoginByCacheKey(@PathVariable("cacheKey") String cacheKey) {
        return success(sysLoginLogService.queryByCacheKey(cacheKey));
    }

    @Operation(summary = "登录日志-批量删除")
    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping("login")
    @Log(description = "删除登录日志", type = LogTypeEnum.DELETE)
    public ApiResponseModel<String> deleteLoginByIds(@RequestBody @NotEmpty(message = "请选择数据") List<String> ids) {
        sysLoginLogService.deleteByIds(ids);
        return success();
    }

    @Operation(summary = "登录日志-清空日志")
    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping("login/clear")
    @Log(description = "清空登录日志", type = LogTypeEnum.DELETE)
    public ApiResponseModel<String> clearLoginLog() {
        sysLoginLogService.clearLog();
        return success();
    }

    @Operation(summary = "登录日志-导出日志")
    @PostMapping("login/export")
    @Log(description = "导出登录日志", type = LogTypeEnum.EXPORT)
    public void exportLoginExcel(@RequestBody SysLogDTO sysLogDTO) {
        List<? extends SysLogVO> sysLogVOS = sysLoginLogService.exportExcel(sysLogDTO);
        ExcelUtils.export(sysLogVOS, SysLogVO.class);
    }

}
