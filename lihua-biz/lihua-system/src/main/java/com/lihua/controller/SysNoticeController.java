package com.lihua.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.annotation.Log;
import com.lihua.entity.SysNotice;
import com.lihua.entity.SysUser;
import com.lihua.enums.LogTypeEnum;
import com.lihua.model.dto.SysNoticeDTO;
import com.lihua.model.vo.SysNoticeVO;
import com.lihua.model.vo.SysUserNoticeVO;
import com.lihua.service.SysNoticeService;
import com.lihua.service.SysUserNoticeService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import com.lihua.model.validation.MaxPageSizeLimit;
import com.lihua.model.web.ApiResponseModel;
import com.lihua.model.web.basecontroller.ApiResponseController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("system/notice")
@Validated
public class SysNoticeController extends ApiResponseController {

    @Resource
    private SysNoticeService sysNoticeService;

    @Resource
    private SysUserNoticeService sysUserNoticeService;

    @PostMapping("page")
    public ApiResponseModel<IPage<SysNotice>> queryPage(@RequestBody @Validated(MaxPageSizeLimit.class) SysNoticeDTO sysNoticeDTO) {
        return success(sysNoticeService.queryPage(sysNoticeDTO));
    }

    @GetMapping("{id}")
    public ApiResponseModel<SysNoticeVO> queryById(@PathVariable("id") String id) {
        return success(sysNoticeService.queryById(id));
    }

    @GetMapping("preview/{id}")
    public ApiResponseModel<SysNoticeVO> preview(@PathVariable("id") String id) {
        return success(sysNoticeService.preview(id));
    }

    @PostMapping
    @Log(description = "保存通知公告", type = LogTypeEnum.SAVE)
    public ApiResponseModel<String> save(@RequestBody @Validated SysNoticeDTO sysNoticeDTO) {
        return success(sysNoticeService.save(sysNoticeDTO));
    }

    @PostMapping("release/{id}")
    @Log(description = "发布通知公告", type = LogTypeEnum.OTHER)
    public ApiResponseModel<String> release(@PathVariable("id") String id) {
        return success(sysNoticeService.release(id));
    }

    @PostMapping("revoke/{id}")
    @Log(description = "撤回通知公告", type = LogTypeEnum.OTHER)
    public ApiResponseModel<String> revoke(@PathVariable("id") String id) {
        return success(sysNoticeService.revoke(id));
    }

    @DeleteMapping
    @Log(description = "删除通知公告", type = LogTypeEnum.DELETE)
    public ApiResponseModel<String> deleteByIds(@RequestBody @NotEmpty(message = "请选择数据") List<String> ids) {
        sysNoticeService.deleteByIds(ids);
     return success();
    }

    @PostMapping("list")
    public ApiResponseModel<IPage<SysUserNoticeVO>> userMessageList(@RequestBody SysNoticeDTO sysNoticeDTO) {
        return success(sysNoticeService.userMessageList(sysNoticeDTO));
    }

    @GetMapping("readInfo/{noticeId}")
    public ApiResponseModel<Map<String, List<SysUser>>> queryReadInfo(@PathVariable("noticeId") String noticeId) {
        return success(sysUserNoticeService.queryReadInfo(noticeId));
    }

    @PostMapping("star/{noticeId}/{star}")
    public ApiResponseModel<String> changeStar(@PathVariable("noticeId") String noticeId, @PathVariable("star") String star) {
        sysUserNoticeService.changeStar(noticeId, star);
        return success();
    }

    @PostMapping("read/{noticeId}")
    public ApiResponseModel<String> changeRead(@PathVariable("noticeId") String noticeId) {
        sysUserNoticeService.changeRead(noticeId);
        return success();
    }

    @GetMapping("unread/count")
    public ApiResponseModel<Integer> queryUnReadCount() {
        return success(sysUserNoticeService.queryUnReadCount());
    }
}
