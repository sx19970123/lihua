package com.lihua.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.annotation.Log;
import com.lihua.entity.system.SysNotice;
import com.lihua.entity.system.SysUser;
import com.lihua.enums.LogTypeEnum;
import com.lihua.model.system.vo.SysNoticeVO;
import com.lihua.model.system.vo.SysUserNoticeVO;
import com.lihua.model.validation.MaxPageSizeLimit;
import com.lihua.model.web.ApiResponseModel;
import com.lihua.model.web.basecontroller.ApiResponseController;
import com.lihua.model.system.dto.SysNoticeDTO;
import com.lihua.service.system.notice.SysNoticeService;
import com.lihua.service.system.user.SysUserNoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "通知公告")
@RestController
@RequestMapping("system/notice")
@Validated
public class SysNoticeController extends ApiResponseController {

    @Resource
    private SysNoticeService sysNoticeService;

    @Resource
    private SysUserNoticeService sysUserNoticeService;

    @Operation(summary = "分页查询")
    @PostMapping("page")
    public ApiResponseModel<IPage<SysNotice>> queryPage(@RequestBody @Validated(MaxPageSizeLimit.class) SysNoticeDTO sysNoticeDTO) {
        return success(sysNoticeService.queryPage(sysNoticeDTO));
    }

    @Operation(summary = "根据主键查询")
    @GetMapping("{id}")
    public ApiResponseModel<SysNoticeVO> queryById(@PathVariable("id") String id) {
        return success(sysNoticeService.queryById(id));
    }

    @Operation(summary = "预览通知公告")
    @GetMapping("preview/{id}")
    public ApiResponseModel<SysNoticeVO> preview(@PathVariable("id") String id) {
        return success(sysNoticeService.preview(id));
    }

    @Operation(summary = "保存通知公告")
    @PostMapping
    @Log(description = "保存通知公告", type = LogTypeEnum.SAVE)
    public ApiResponseModel<String> save(@RequestBody @Validated SysNoticeDTO sysNoticeDTO) {
        return success(sysNoticeService.save(sysNoticeDTO));
    }

    @Operation(summary = "发布通知公告")
    @PostMapping("release/{id}")
    @Log(description = "发布通知公告", type = LogTypeEnum.OTHER)
    public ApiResponseModel<String> release(@PathVariable("id") String id) {
        return success(sysNoticeService.release(id));
    }

    @Operation(summary = "撤回通知公告")
    @PostMapping("revoke/{id}")
    @Log(description = "撤回通知公告", type = LogTypeEnum.OTHER)
    public ApiResponseModel<String> revoke(@PathVariable("id") String id) {
        return success(sysNoticeService.revoke(id));
    }

    @Operation(summary = "删除通知公告")
    @DeleteMapping
    @Log(description = "删除通知公告", type = LogTypeEnum.DELETE)
    public ApiResponseModel<String> deleteByIds(@RequestBody @NotEmpty(message = "请选择数据") List<String> ids) {
        sysNoticeService.deleteByIds(ids);
     return success();
    }

    @Operation(summary = "用户查询消息通知")
    @PostMapping("list/{userId}")
    public ApiResponseModel<IPage<SysUserNoticeVO>> queryListByUserId(@PathVariable("userId") String userId, @RequestBody SysNoticeDTO sysNoticeDTO) {
        return success(sysNoticeService.queryListByUserId(userId, sysNoticeDTO));
    }

    @Operation(summary = "获取notice已读未读用户")
    @GetMapping("readInfo/{noticeId}")
    public ApiResponseModel<Map<String, List<SysUser>>> queryReadInfo(@PathVariable("noticeId") String noticeId) {
        return success(sysUserNoticeService.queryReadInfo(noticeId));
    }

    @Operation(summary = "用户添加star")
    @PostMapping("star/{noticeId}/{star}")
    public ApiResponseModel<String> changeStar(@PathVariable("noticeId") String noticeId, @PathVariable("star") String star) {
        sysUserNoticeService.changeStar(noticeId, star);
        return success();
    }

    @Operation(summary = "用户已读")
    @PostMapping("read/{noticeId}")
    public ApiResponseModel<String> changeRead(@PathVariable("noticeId") String noticeId) {
        sysUserNoticeService.changeRead(noticeId);
        return success();
    }

    @Operation(summary = "获取未读消息总数")
    @GetMapping("unread/count")
    public ApiResponseModel<Integer> queryUnReadCount() {
        return success(sysUserNoticeService.queryUnReadCount());
    }
}
