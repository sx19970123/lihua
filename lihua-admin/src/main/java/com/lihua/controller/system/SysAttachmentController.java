package com.lihua.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.annotation.Log;
import com.lihua.entity.system.SysAttachment;
import com.lihua.enums.LogTypeEnum;
import com.lihua.model.system.vo.SysAttachmentVO;
import com.lihua.model.web.ApiResponseModel;
import com.lihua.model.web.basecontroller.ApiResponseController;
import com.lihua.model.system.dto.SysAttachmentDTO;
import com.lihua.service.system.attachment.SysAttachmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;

@Tag(name = "附件管理")
@Slf4j
@RestController
@RequestMapping("system/attachment")
public class SysAttachmentController extends ApiResponseController {

    @Resource
    private SysAttachmentService sysAttachmentService;

    @Operation(summary = "分页查询附件")
    @PostMapping("page")
    public ApiResponseModel<IPage<SysAttachment>> queryPage(@RequestBody SysAttachmentDTO sysAttachmentDTO) {
        return success(sysAttachmentService.queryPage(sysAttachmentDTO));
    }

    @Operation(summary = "根据主键查询附件详情")
    @GetMapping("{id}")
    public ApiResponseModel<SysAttachmentVO> queryById(@PathVariable("id") String id) {
        return success(sysAttachmentService.queryById(id));
    }

    @Operation(summary = "附件删除")
    @DeleteMapping
    @Log(description = "附件删除", type = LogTypeEnum.DELETE)
    @PreAuthorize("hasRole('ROLE_admin')")
    public ApiResponseModel<String> delete(@RequestBody @NotEmpty(message = "附件id为空") List<String> ids) {
        sysAttachmentService.deleteByIds(ids);
        return success();
    }

    @Operation(summary = "附件删除（强制）")
    @DeleteMapping("force/{id}")
    @Log(description = "附件删除（强制）", type = LogTypeEnum.DELETE)
    @PreAuthorize("hasRole('ROLE_admin')")
    public ApiResponseModel<String> forceDelete(@PathVariable("id") String id) {
        sysAttachmentService.forceDelete(Collections.singletonList(id));
        return success();
    }

    @Operation(summary = "获取附件下载链接")
    @GetMapping("url/{id}")
    public ApiResponseModel<String> getDownloadURL(@PathVariable("id") String id, Integer expireTime) {
        return success(sysAttachmentService.getDownloadURL(id, expireTime));
    }
}
