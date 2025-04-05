package com.lihua.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.annotation.Log;
import com.lihua.enums.LogTypeEnum;
import com.lihua.model.excel.ExcelImportResult;
import com.lihua.model.validation.MaxPageSizeLimit;
import com.lihua.model.web.ApiResponseModel;
import com.lihua.model.web.basecontroller.ApiResponseController;
import com.lihua.entity.system.SysPost;
import com.lihua.model.system.dto.SysPostDTO;
import com.lihua.model.system.vo.SysPostVO;
import com.lihua.service.system.post.SysPostService;
import com.lihua.utils.excel.ExcelUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import lombok.SneakyThrows;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Tag(name = "岗位管理")
@RestController
@RequestMapping("system/post")
@Validated
public class SysPostController extends ApiResponseController {

    @Resource
    private SysPostService sysPostService;


    @Operation(summary = "分页查询")
    @PostMapping("page")
    public ApiResponseModel<IPage<SysPostVO>> queryPage(@RequestBody @Validated(MaxPageSizeLimit.class) SysPostDTO dto) {
        return success(sysPostService.queryPage(dto));
    }

    @Operation(summary = "根据id查询")
    @GetMapping("{id}")
    public ApiResponseModel<SysPost> queryById(@PathVariable("id") String id) {
        return success(sysPostService.queryById(id));
    }

    @Operation(summary = "保存岗位信息")
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping
    @Log(description = "保存岗位信息", type = LogTypeEnum.SAVE)
    public ApiResponseModel<String> save(@RequestBody @Validated SysPost sysPost) {
        return success(sysPostService.savePost(sysPost));
    }

    @Operation(summary = "更新岗位状态")
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("updateStatus/{id}/{currentStatus}")
    @Log(description = "更新岗位状态", type = LogTypeEnum.UPDATE_STATUS)
    public ApiResponseModel<String> updateStatus(@PathVariable("id") String id, @PathVariable("currentStatus") String currentStatus) {
        return success(sysPostService.updateStatus(id, currentStatus));
    }

    @Operation(summary = "删除岗位数据")
    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping
    @Log(description = "删除岗位数据", type = LogTypeEnum.DELETE)
    public ApiResponseModel<String> deleteByIds(@RequestBody @NotEmpty(message = "请选中要删除的数据") List<String> ids) {
        sysPostService.deleteByIds(ids);
        return success();
    }

    @Operation(summary = "根据部门id查询岗位集合")
    @PostMapping("option")
    public ApiResponseModel<Map<String, List<SysPost>>> getPostOptionByDeptId(@RequestBody @NotEmpty(message = "部门集合为空") List<String> deptIds) {
        return success(sysPostService.getPostOptionByDeptId(deptIds));
    }

    @Operation(summary = "导出岗位数据")
    @PostMapping("export")
    @Log(description = "导出岗位数据", type = LogTypeEnum.EXPORT)
    public ApiResponseModel<String> exportExcel(SysPostDTO dto) {
        String path = sysPostService.exportExcel(dto);
        return success(path);
    }

    @Operation(summary = "导入岗位数据")
    @SneakyThrows
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("import")
    @Log(description = "导入岗位数据", type = LogTypeEnum.IMPORT)
    public ApiResponseModel<ExcelImportResult> importExcel(@RequestParam("file") MultipartFile file) {
        List<SysPostVO> sysPostVOList = ExcelUtils.importExport(file, SysPostVO.class, 0);
        return success(sysPostService.importExcel(sysPostVOList));
    }
}
