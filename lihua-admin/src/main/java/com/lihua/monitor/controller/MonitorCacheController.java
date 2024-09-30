package com.lihua.monitor.controller;

import com.lihua.model.web.BaseController;
import com.lihua.monitor.service.MonitorCacheService;
import jakarta.annotation.Resource;
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

    @GetMapping("info/{key}")
    public String cacheInfo(@PathVariable("key") String key) {
        return success(monitorCacheService.cacheInfo(key));
    }

    @DeleteMapping("prefix/{keyPrefix}")
    public String removeByKeyPrefix(@PathVariable("keyPrefix") String keyPrefix) {
        monitorCacheService.removeByKeyPrefix(keyPrefix);
        return success();
    }

    @DeleteMapping("key/{key}")
    public String remove(@PathVariable("key") String key) {
        monitorCacheService.remove(key);
        return success();
    }
}
