package com.lihua.monitor.controller;

import com.lihua.common.model.response.ResponseModel;
import com.lihua.common.model.response.ResponseController;
import com.lihua.common.model.response.Responses;
import com.lihua.log.annotation.Log;
import com.lihua.log.enums.LogTypeEnum;
import com.lihua.monitor.model.CacheMonitor;
import com.lihua.monitor.service.MonitorCacheService;
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
public class MonitorCacheController extends ResponseController {

    @Resource
    private MonitorCacheService monitorCacheService;

    @Operation(summary = "内存占用量")
    @GetMapping("memory")
    public ResponseModel<String> memoryInfo() {
        return Responses.success(monitorCacheService.memoryInfo());
    }

    @Operation(summary = "缓存组列表")
    @GetMapping("group")
    public ResponseModel<List<CacheMonitor>> cacheKeyGroups() {
        return Responses.success(monitorCacheService.cacheKeyGroups());
    }

    @Operation(summary = "根据前缀查询所有key")
    @GetMapping("prefix/{keyPrefix}")
    public ResponseModel<Set<String>> cacheKeys(@PathVariable("keyPrefix") String keyPrefix) {
        return Responses.success(monitorCacheService.cacheKeys(keyPrefix));
    }

    @Operation(summary = "根据key获取缓存信息")
    @PostMapping("info")
    public ResponseModel<CacheMonitor> cacheInfo(@RequestBody @Valid CacheMonitor cacheMonitor) {
        return Responses.success(monitorCacheService.cacheInfo(cacheMonitor.getKey()));
    }

    @Operation(summary = "删除缓存")
    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping("key")
    @Log(description = "删除缓存", type = LogTypeEnum.DELETE)
    public ResponseModel<String> remove(@RequestBody @Valid CacheMonitor cacheMonitor) {
        monitorCacheService.remove(cacheMonitor.getKey());
        return Responses.success();
    }
}
