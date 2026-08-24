package com.lihua.controller.app;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.common.model.response.ApiResponseModel;
import com.lihua.common.model.response.basecontroller.ApiResponseController;
import com.lihua.model.dto.SysNoticeDTO;
import com.lihua.model.vo.SysNoticeVO;
import com.lihua.model.vo.SysUserNoticeVO;
import com.lihua.service.SysNoticeService;
import com.lihua.service.SysUserNoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "APP-通知公告")
@RestController
@RequestMapping("app/system/notice")
@Validated
public class AppSysNoticeController extends ApiResponseController {

    @Resource
    private SysNoticeService sysNoticeService;

    @Resource
    private SysUserNoticeService sysUserNoticeService;

    @Operation(summary = "预览通知公告")
    @GetMapping("preview/{id}")
    public ApiResponseModel<SysNoticeVO> preview(@PathVariable("id") String id) {
        return success(sysNoticeService.preview(id));
    }

    @Operation(summary = "列表查询")
    @PostMapping("list")
    public ApiResponseModel<IPage<SysUserNoticeVO>> userMessageList(@RequestBody SysNoticeDTO sysNoticeDTO) {
        return success(sysNoticeService.userMessageList(sysNoticeDTO));
    }

    @Operation(summary = "通知公告标星")
    @PostMapping("star/{noticeId}/{star}")
    public ApiResponseModel<String> changeStar(@PathVariable("noticeId") String noticeId, @PathVariable("star") String star) {
        sysUserNoticeService.changeStar(noticeId, star);
        return success();
    }

    @Operation(summary = "已读")
    @PostMapping("read/{noticeId}")
    public ApiResponseModel<String> changeRead(@PathVariable("noticeId") String noticeId) {
        sysUserNoticeService.changeRead(noticeId);
        return success();
    }

    @Operation(summary = "未读数量查询")
    @GetMapping("unread/count")
    public ApiResponseModel<Integer> queryUnReadCount() {
        return success(sysUserNoticeService.queryUnReadCount());
    }
}
