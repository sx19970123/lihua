package com.lihua.controller;

import com.lihua.annotation.Log;
import com.lihua.enums.LogTypeEnum;
import com.lihua.model.CacheMonitor;
import model.web.ApiResponseModel;
import model.web.basecontroller.ApiResponseController;
import model.web.response.ApiResponse;
import com.lihua.service.MonitorCacheService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("monitor/cache")
public class MonitorCacheController extends ApiResponseController {

    @Resource
    private MonitorCacheService monitorCacheService;

    @GetMapping("memory")
    public ApiResponseModel<String> memoryInfo() {
        return ApiResponse.success(monitorCacheService.memoryInfo());
    }

    @GetMapping("group")
    public ApiResponseModel<List<CacheMonitor>> cacheKeyGroups() {
        return ApiResponse.success(monitorCacheService.cacheKeyGroups());
    }

    @GetMapping("prefix/{keyPrefix}")
    public ApiResponseModel<Set<String>> cacheKeys(@PathVariable("keyPrefix") String keyPrefix) {
        return ApiResponse.success(monitorCacheService.cacheKeys(keyPrefix));
    }

    @PostMapping("info")
    public ApiResponseModel<CacheMonitor> cacheInfo(@RequestBody @Valid CacheMonitor cacheMonitor) {
        return ApiResponse.success(monitorCacheService.cacheInfo(cacheMonitor.getKey()));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping("key")
    @Log(description = "删除缓存", type = LogTypeEnum.DELETE)
    public ApiResponseModel<String> remove(@RequestBody @Valid CacheMonitor cacheMonitor) {
        monitorCacheService.remove(cacheMonitor.getKey());
        return ApiResponse.success();
    }
}
