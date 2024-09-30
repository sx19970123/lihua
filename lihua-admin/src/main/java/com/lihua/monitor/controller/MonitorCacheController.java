package com.lihua.monitor.controller;

import com.lihua.annotation.Log;
import com.lihua.enums.LogTypeEnum;
import com.lihua.model.web.BaseController;
import com.lihua.monitor.model.CacheMonitor;
import com.lihua.monitor.service.MonitorCacheService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("monitor/cache")
public class MonitorCacheController extends BaseController {

    @Resource
    private MonitorCacheService monitorCacheService;

    @GetMapping("memory")
    public String memoryInfo() {
        return success(monitorCacheService.memoryInfo());
    }

    @GetMapping("group")
    public String cacheKeyGroups() {
        return success(monitorCacheService.cacheKeyGroups());
    }

    @GetMapping("prefix/{keyPrefix}")
    public String cacheKeys(@PathVariable("keyPrefix") String keyPrefix) {
        return success(monitorCacheService.cacheKeys(keyPrefix));
    }

    @PostMapping("info")
    public String cacheInfo(@RequestBody @Valid CacheMonitor cacheMonitor) {
        return success(monitorCacheService.cacheInfo(cacheMonitor.getKey()));
    }

    @DeleteMapping("key")
    @Log(description = "删除缓存", type = LogTypeEnum.DELETE)
    public String remove(@RequestBody @Valid CacheMonitor cacheMonitor) {
        monitorCacheService.remove(cacheMonitor.getKey());
        return success();
    }
}
