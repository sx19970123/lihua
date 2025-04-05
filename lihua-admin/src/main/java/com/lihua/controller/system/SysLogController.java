package com.lihua.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.annotation.Log;
import com.lihua.enums.LogTypeEnum;
import com.lihua.model.system.vo.SysLogVO;
import com.lihua.model.validation.MaxPageSizeLimit;
import com.lihua.model.web.ApiResponseModel;
import com.lihua.model.web.basecontroller.ApiResponseController;
import com.lihua.model.system.dto.SysLogDTO;
import com.lihua.service.system.log.SysLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    @Operation(summary = "获取日志类型下拉选项")
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
    @Operation(summary = "查询操作日志列表")
    @PostMapping("operate/page")
    public ApiResponseModel<IPage<? extends SysLogVO>> queryOperatePage(@RequestBody @Validated(MaxPageSizeLimit.class) SysLogDTO sysLogDTO) {
        return success(sysOperateLogService.queryPage(sysLogDTO));
    }

    @Operation(summary = "根据主键查询操作日志")
    @GetMapping("operate/{id}")
    public ApiResponseModel<SysLogVO> queryOperateById(@PathVariable("id") String id) {
        return success(sysOperateLogService.queryById(id));
    }

    @Operation(summary = "删除操作日志")
    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping("operate")
    @Log(description = "删除操作日志", type = LogTypeEnum.DELETE)
    public ApiResponseModel<String> deleteOperateByIds(@RequestBody @NotEmpty(message = "请选择数据") List<String> ids) {
        sysOperateLogService.deleteByIds(ids);
        return success();
    }

    @Operation(summary = "清空操作日志")
    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping("operate/clear")
    @Log(description = "清空操作日志", type = LogTypeEnum.DELETE)
    public ApiResponseModel<String> clearOperateLog() {
        sysOperateLogService.clearLog();
        return success();
    }

    @Operation(summary = "导出操作日志")
    @PostMapping("operate/export")
    @Log(description = "导出操作日志", type = LogTypeEnum.EXPORT)
    public ApiResponseModel<String> exportOperateExcel(@RequestBody SysLogDTO sysLogDTO) {
        String path = sysOperateLogService.exportExcel(sysLogDTO);
        return success(path);
    }


    // 登录日志------------------------------------------------------------

    @Operation(summary = "查询登录日志列表")
    @PostMapping("login/page")
    public ApiResponseModel<IPage<? extends SysLogVO>> queryLoginPage(@RequestBody @Validated(MaxPageSizeLimit.class) SysLogDTO sysLogDTO) {
        return success(sysLoginLogService.queryPage(sysLogDTO));
    }

    @Operation(summary = "根据主键查询登录日志")
    @GetMapping("login/{id}")
    public ApiResponseModel<SysLogVO> queryLoginById(@PathVariable("id") String id) {
        return success(sysLoginLogService.queryById(id));
    }

    @Operation(summary = "根据缓存查询登录日志")
    @GetMapping("login/cacheKey/{cacheKey}")
    public ApiResponseModel<SysLogVO> queryLoginByCacheKey(@PathVariable("cacheKey") String cacheKey) {
        return success(sysLoginLogService.queryByCacheKey(cacheKey));
    }

    @Operation(summary = "删除登录日志")
    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping("login")
    @Log(description = "删除登录日志", type = LogTypeEnum.DELETE)
    public ApiResponseModel<String> deleteLoginByIds(@RequestBody @NotEmpty(message = "请选择数据") List<String> ids) {
        sysLoginLogService.deleteByIds(ids);
        return success();
    }

    @Operation(summary = "清空登录日志")
    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping("login/clear")
    @Log(description = "清空登录日志", type = LogTypeEnum.DELETE)
    public ApiResponseModel<String> clearLoginLog() {
        sysLoginLogService.clearLog();
        return success();
    }

    @Operation(summary = "导出登录日志")
    @PostMapping("login/export")
    @Log(description = "导出登录日志", type = LogTypeEnum.EXPORT)
    public ApiResponseModel<String> exportLoginExcel(@RequestBody SysLogDTO sysLogDTO) {
        String path = sysLoginLogService.exportExcel(sysLogDTO);
        return success(path);
    }

}
