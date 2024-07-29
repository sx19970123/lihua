package com.lihua.system.controller;

import com.lihua.model.web.BaseController;
import com.lihua.system.entity.SysNotice;
import com.lihua.system.model.dto.SysNoticeDTO;
import com.lihua.system.service.SysNoticeService;
import com.lihua.system.service.SysUserNoticeService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("system/notice")
public class SysNoticeController extends BaseController {

    @Resource
    private SysNoticeService sysNoticeService;

    @Resource
    private SysUserNoticeService sysUserNoticeService;

    @PostMapping("page")
    public String findPage(@RequestBody SysNoticeDTO sysNoticeDTO) {
        return success(sysNoticeService.findPage(sysNoticeDTO));
    }

    @GetMapping("{id}")
    public String findById(@PathVariable("id") String id) {
        return success(sysNoticeService.findById(id));
    }

    @PostMapping
    public String save(@RequestBody SysNoticeDTO sysNoticeDTO) {
        return success(sysNoticeService.save(sysNoticeDTO));
    }

    @PostMapping("release/{id}")
    public String release(@PathVariable("id") String id) {
        return success(sysNoticeService.release(id));
    }

    @PostMapping("revoke/{id}")
    public String revoke(@PathVariable("id") String id) {
        return success(sysNoticeService.revoke(id));
    }

    @GetMapping("readInfo/{id}")
    public String findReadInfo(@PathVariable("id") String id) {
        return success(sysUserNoticeService.findReadInfo(id));
    }

    @DeleteMapping
    public String deleteByIds(@RequestBody @NotEmpty(message = "请选择数据") List<String> ids) {
        sysNoticeService.deleteByIds(ids);
     return success();
    }

    @PostMapping("list/{userId}")
    public String findListByUserId(@PathVariable("userId") String userId, @RequestBody SysNoticeDTO sysNoticeDTO) {
        return success(sysNoticeService.findListByUserId(userId, sysNoticeDTO));
    }
}
