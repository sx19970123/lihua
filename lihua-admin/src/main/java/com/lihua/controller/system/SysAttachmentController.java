package com.lihua.controller.system;

import com.lihua.annotation.Log;
import com.lihua.enums.LogTypeEnum;
import com.lihua.model.web.BaseController;
import com.lihua.model.system.dto.SysAttachmentDTO;
import com.lihua.service.system.attachment.SysAttachmentService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("system/attachment")
public class SysAttachmentController extends BaseController {

    @Resource
    private SysAttachmentService sysAttachmentService;

    @PostMapping("page")
    public String queryPage(@RequestBody SysAttachmentDTO sysAttachmentDTO) {
        return success(sysAttachmentService.queryPage(sysAttachmentDTO));
    }

    @GetMapping("{id}")
    public String queryById(@PathVariable("id") String id) {
        return success(sysAttachmentService.queryById(id));
    }

    @DeleteMapping
    @Log(description = "附件删除", type = LogTypeEnum.DELETE)
    @PreAuthorize("hasRole('ROLE_admin')")
    public String delete(@RequestBody @NotEmpty(message = "附件id为空") List<String> ids) {
        sysAttachmentService.deleteByIds(ids);
        return success();
    }

    @DeleteMapping("force/{id}")
    @Log(description = "附件删除（强制）", type = LogTypeEnum.DELETE)
    @PreAuthorize("hasRole('ROLE_admin')")
    public String forceDelete(@PathVariable("id") String id) {
        sysAttachmentService.forceDelete(Collections.singletonList(id));
        return success();
    }

    @GetMapping("url/{id}")
    public String getDownloadURL(@PathVariable("id") String id, Integer expireTime) {
        return success(sysAttachmentService.getDownloadURL(id, expireTime));
    }
}
