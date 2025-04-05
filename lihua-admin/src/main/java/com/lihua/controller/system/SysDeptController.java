package com.lihua.controller.system;

import com.lihua.annotation.Log;
import com.lihua.enums.LogTypeEnum;
import com.lihua.model.excel.ExcelImportResult;
import com.lihua.model.web.ApiResponseModel;
import com.lihua.model.web.basecontroller.ApiResponseController;
import com.lihua.entity.system.SysDept;
import com.lihua.model.system.vo.SysDeptVO;
import com.lihua.service.system.dept.SysDeptService;
import com.lihua.utils.excel.ExcelUtils;
import com.lihua.utils.tree.TreeUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "部门管理")
@RestController
@RequestMapping("system/dept")
@Validated
public class SysDeptController extends ApiResponseController {

    @Resource
    private SysDeptService sysDeptService;

    @Operation(summary = "查询带有岗位信息的post列表")
    @PostMapping("list")
    public ApiResponseModel<List<SysDeptVO>> queryDeptPostList(@RequestBody SysDept sysDept) {
        List<SysDeptVO> deptPostList = sysDeptService.queryDeptPostList(sysDept);
        return success(TreeUtils.buildTree(deptPostList));
    }

    @Operation(summary = "保存部门数据")
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping
    @Log(description = "保存部门数据", type = LogTypeEnum.SAVE)
    public ApiResponseModel<String> save(@RequestBody @Validated SysDept sysDept) {
        return success(sysDeptService.saveDept(sysDept));
    }

    @Operation(summary = "根据id查询部门")
    @GetMapping("{id}")
    public ApiResponseModel<SysDept> queryById(@PathVariable("id") String id) {
        return success(sysDeptService.queryById(id));
    }

    @Operation(summary = "更新部门状态")
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("updateStatus/{id}/{currentStatus}")
    @Log(description = "更新部门状态", type = LogTypeEnum.UPDATE_STATUS)
    public ApiResponseModel<String> updateStatus(@PathVariable("id") String id, @PathVariable("currentStatus") String currentStatus) {
        return success(sysDeptService.updateStatus(id, currentStatus));
    }

    @Operation(summary = "删除部门数据")
    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping
    @Log(description = "删除部门数据", type = LogTypeEnum.DELETE)
    public ApiResponseModel<String> deleteByIds(@RequestBody @NotEmpty(message = "请选择数据") List<String> ids) {
        sysDeptService.deleteByIds(ids);
        return success();
    }

    @Operation(summary = "菜单下拉框选项")
    @GetMapping("option")
    public ApiResponseModel<List<SysDept>> deptTreeOption() {
        return success(sysDeptService.deptTreeOption());
    }

    @Operation(summary = "批量导出部门")
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("export")
    @Log(description = "批量导出部门", type = LogTypeEnum.EXPORT)
    public ApiResponseModel<String> exportExcel(@RequestBody SysDept sysDept) {
        String path = sysDeptService.exportExcel(sysDept);
        return success(path);
    }

    @Operation(summary = "批量入部门")
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("import")
    @Log(description = "批量入部门", type = LogTypeEnum.IMPORT)
    public ApiResponseModel<ExcelImportResult> importExcel(@RequestParam("file") MultipartFile file) {
        List<SysDeptVO> sysUserVOS = ExcelUtils.importExport(file, SysDeptVO.class, 0);
        return success(sysDeptService.importExcel(sysUserVOS));
    }
}

