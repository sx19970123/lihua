package com.lihua.system.controller;

import com.lihua.model.web.BaseController;
import com.lihua.system.entity.SysStarView;
import com.lihua.system.service.SysStarViewService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("system/star/view")
public class SysStarViewController extends BaseController {

    @Resource
    private SysStarViewService  sysStarViewService;

    @PostMapping
    public String save(@RequestBody SysStarView sysStarView) {
        return success(sysStarViewService.save(sysStarView));
    }
}
