package com.lihua.controller;

import com.lihua.annotation.Log;
import com.lihua.enums.LogTypeEnum;
import com.lihua.model.CacheMonitor;
import model.web.ApiResponseModel;
import model.web.basecontroller.ApiResponseController;
import model.web.response.ApiResponse;
import com.lihua.service.MonitorCacheService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Tag(name = "缓存监控")
@RestController
@RequestMapping("monitor/cache")
public class MonitorCacheController extends ApiResponseController {

    @Resource
    private MonitorCacheService monitorCacheService;

    @Operation(summary = "内存占用")
    @GetMapping("memory")
    public ApiResponseModel<String> memoryInfo() {
        return ApiResponse.success(monitorCacheService.memoryInfo());
    }

    @Operation(summary = "缓存组列表")
    @GetMapping("group")
    public ApiResponseModel<List<CacheMonitor>> cacheKeyGroups() {
        return ApiResponse.success(monitorCacheService.cacheKeyGroups());
    }

    @Operation(summary = "根据key前缀获取所有相关 key")
    @GetMapping("prefix/{keyPrefix}")
    public ApiResponseModel<Set<String>> cacheKeys(@PathVariable("keyPrefix") String keyPrefix) {
        return ApiResponse.success(monitorCacheService.cacheKeys(keyPrefix));
    }

    @Operation(summary = "根据key获取缓存信息")
    @PostMapping("info")
    public ApiResponseModel<CacheMonitor> cacheInfo(@RequestBody @Valid CacheMonitor cacheMonitor) {
        return ApiResponse.success(monitorCacheService.cacheInfo(cacheMonitor.getKey()));
    }

    @Operation(summary = "根据前缀删除缓存")
    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping("key")
    @Log(description = "删除缓存", type = LogTypeEnum.DELETE)
    public ApiResponseModel<String> remove(@RequestBody @Valid CacheMonitor cacheMonitor) {
        monitorCacheService.remove(cacheMonitor.getKey());
        return ApiResponse.success();
    }
}
