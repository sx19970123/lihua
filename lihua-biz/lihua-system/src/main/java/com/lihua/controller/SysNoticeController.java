package com.lihua.controller;

import com.lihua.controller.base.BaseSysNoticeController;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.common.model.response.ApiResponseModel;
import com.lihua.entity.SysNotice;
import com.lihua.entity.SysUser;
import com.lihua.log.annotation.Log;
import com.lihua.log.enums.LogTypeEnum;
import com.lihua.model.dto.SysNoticeDTO;
import com.lihua.model.dto.NoticeReadInfoDTO;
import com.lihua.model.vo.SysNoticeVO;
import com.lihua.mybatis.model.validation.MaxPageSizeLimit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.groups.Default;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "通知公告")
@RestController
@RequestMapping("system/notice")
@Validated
public class SysNoticeController extends BaseSysNoticeController {

    @Operation(summary = "分页查询")
    @PostMapping("page")
    public ApiResponseModel<IPage<SysNotice>> queryPage(@RequestBody @Validated(MaxPageSizeLimit.class) SysNoticeDTO sysNoticeDTO) {
        return success(sysNoticeService.queryPage(sysNoticeDTO));
    }

    @Operation(summary = "根据id查询详情")
    @GetMapping("{id}")
    public ApiResponseModel<SysNoticeVO> queryById(@PathVariable("id") String id) {
        return success(sysNoticeService.queryById(id));
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

    @Operation(summary = "撤销通知公告")
    @PostMapping("revoke/{id}")
    @Log(description = "撤销通知公告", type = LogTypeEnum.OTHER)
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

    @Operation(summary = "分页查询已读/未读用户")
    @PostMapping("readInfo")
    public ApiResponseModel<IPage<SysUser>> queryReadInfo(@RequestBody @Validated({Default.class, MaxPageSizeLimit.class}) NoticeReadInfoDTO readInfoDTO) {
        return success(sysUserNoticeService.queryReadInfo(readInfoDTO));
    }
}
