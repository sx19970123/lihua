package com.lihua.system.controller;

import com.lihua.annotation.Log;
import com.lihua.enums.LogTypeEnum;
import com.lihua.model.validation.MaxPageSizeLimit;
import com.lihua.model.web.BaseController;
import com.lihua.system.entity.SysPost;
import com.lihua.system.model.dto.SysPostDTO;
import com.lihua.system.model.vo.SysPostVO;
import com.lihua.system.service.SysPostService;
import com.lihua.utils.excel.ExcelUtils;
import com.lihua.utils.file.FileDownloadUtils;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import lombok.SneakyThrows;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("system/post")
public class SysPostController extends BaseController {

    @Resource
    private SysPostService sysPostService;


    @PostMapping("page")
    public String findPage(@RequestBody @Validated(MaxPageSizeLimit.class) SysPostDTO dto) {
        return success(sysPostService.findPage(dto));
    }

    @GetMapping("{id}")
    public String findById(@PathVariable("id") String id) {
        return success(sysPostService.findById(id));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping
    @Log(description = "保存岗位信息", type = LogTypeEnum.SAVE)
    public String save(@RequestBody @Validated SysPost sysPost) {
        return success(sysPostService.savePost(sysPost));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("updateStatus/{id}/{currentStatus}")
    @Log(description = "更新岗位状态", type = LogTypeEnum.UPDATE_STATUS)
    public String updateStatus(@PathVariable("id") String id,@PathVariable("currentStatus") String currentStatus) {
        return success(sysPostService.updateStatus(id, currentStatus));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping
    @Log(description = "删除岗位数据", type = LogTypeEnum.DELETE)
    public String deleteByIds(@RequestBody @NotEmpty(message = "请选中要删除的数据") List<String> ids) {
        sysPostService.deleteByIds(ids);
        return success();
    }

    @PostMapping("option")
    public String getPostOptionByDeptId(@RequestBody @NotEmpty(message = "部门集合为空") List<String> deptIds) {
        return success(sysPostService.getPostOptionByDeptId(deptIds));
    }

    @PostMapping("export")
    @Log(description = "导出岗位数据", type = LogTypeEnum.EXPORT)
    public String exportExcel(SysPostDTO dto) {
        String path = sysPostService.exportExcel(dto);
        return success(FileDownloadUtils.addToDownloadableList(path));
    }

    @SneakyThrows
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("import")
    @Log(description = "导入岗位数据", type = LogTypeEnum.IMPORT)
    public String importExcel(@RequestParam("file") MultipartFile file) {
        List<SysPostVO> sysPostVOList = ExcelUtils.importExport(file, SysPostVO.class, 0);
        return success(sysPostService.importExcel(sysPostVOList));
    }
}
