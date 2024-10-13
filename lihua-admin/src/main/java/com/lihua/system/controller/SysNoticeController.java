package com.lihua.system.controller;

import com.lihua.annotation.Log;
import com.lihua.enums.LogTypeEnum;
import com.lihua.model.validation.MaxPageSizeLimit;
import com.lihua.model.web.BaseController;
import com.lihua.system.model.dto.SysNoticeDTO;
import com.lihua.system.service.SysNoticeService;
import com.lihua.system.service.SysUserNoticeService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;
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
    public String findPage(@RequestBody @Validated(MaxPageSizeLimit.class) SysNoticeDTO sysNoticeDTO) {
        return success(sysNoticeService.findPage(sysNoticeDTO));
    }

    @GetMapping("{id}")
    public String findById(@PathVariable("id") String id) {
        return success(sysNoticeService.findById(id));
    }

    @PostMapping
    @Log(description = "保存通知公告", type = LogTypeEnum.SAVE)
    public String save(@RequestBody @Validated SysNoticeDTO sysNoticeDTO) {
        return success(sysNoticeService.save(sysNoticeDTO));
    }

    @PostMapping("release/{id}")
    @Log(description = "发布通知公告", type = LogTypeEnum.OTHER)
    public String release(@PathVariable("id") String id) {
        return success(sysNoticeService.release(id));
    }

    @PostMapping("revoke/{id}")
    @Log(description = "撤回通知公告", type = LogTypeEnum.OTHER)
    public String revoke(@PathVariable("id") String id) {
        return success(sysNoticeService.revoke(id));
    }

    @DeleteMapping
    @Log(description = "删除通知公告", type = LogTypeEnum.DELETE)
    public String deleteByIds(@RequestBody @NotEmpty(message = "请选择数据") List<String> ids) {
        sysNoticeService.deleteByIds(ids);
     return success();
    }

    @PostMapping("list/{userId}")
    public String findListByUserId(@PathVariable("userId") String userId, @RequestBody SysNoticeDTO sysNoticeDTO) {
        return success(sysNoticeService.findListByUserId(userId, sysNoticeDTO));
    }

    @GetMapping("readInfo/{noticeId}")
    public String findReadInfo(@PathVariable("noticeId") String noticeId) {
        return success(sysUserNoticeService.findReadInfo(noticeId));
    }

    @PostMapping("star/{noticeId}/{star}")
    public String changeStar(@PathVariable("noticeId") String noticeId, @PathVariable("star") String star) {
        sysUserNoticeService.changeStar(noticeId, star);
        return success();
    }

    @PostMapping("read/{noticeId}")
    public String changeRead(@PathVariable("noticeId") String noticeId) {
        sysUserNoticeService.changeRead(noticeId);
        return success();
    }

    @GetMapping("unread/count")
    public String findUnReadCount() {
        return success(sysUserNoticeService.findUnReadCount());
    }
}
