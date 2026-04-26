package com.lihua.controller;

import com.lihua.common.model.response.ApiResponseModel;
import com.lihua.common.model.response.basecontroller.ApiResponseController;
import com.lihua.common.utils.tree.TreeUtils;
import com.lihua.entity.SysDept;
import com.lihua.excel.utils.ExcelUtils;
import com.lihua.log.annotation.Log;
import com.lihua.log.enums.LogTypeEnum;
import com.lihua.model.vo.SysDeptVO;
import com.lihua.service.SysDeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "部门管理")
@RestController
@RequestMapping("system/dept")
@Validated
public class SysDeptController extends ApiResponseController {

    @Resource
    private SysDeptService sysDeptService;

    @Operation(summary = "分页查询")
    @PostMapping("list")
    public ApiResponseModel<List<SysDeptVO>> queryDeptPostList(@RequestBody SysDept sysDept) {
        List<SysDeptVO> deptPostList = sysDeptService.queryDeptPostList(sysDept);
        return success(TreeUtils.buildTree(deptPostList));
    }

    @Operation(summary = "保存数据")
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping
    @Log(description = "保存部门数据", type = LogTypeEnum.SAVE)
    public ApiResponseModel<String> save(@RequestBody @Validated SysDept sysDept) {
        return success(sysDeptService.saveDept(sysDept));
    }

    @Operation(summary = "根据id查询详情")
    @GetMapping("{id}")
    public ApiResponseModel<SysDept> queryById(@PathVariable("id") String id) {
        return success(sysDeptService.queryById(id));
    }

    @Operation(summary = "更新状态")
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("updateStatus/{id}/{currentStatus}")
    @Log(description = "更新部门状态", type = LogTypeEnum.UPDATE_STATUS)
    public ApiResponseModel<String> updateStatus(@PathVariable("id") String id, @PathVariable("currentStatus") String currentStatus) {
        return success(sysDeptService.updateStatus(id, currentStatus));
    }

    @Operation(summary = "批量删除")
    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping
    @Log(description = "删除部门数据", type = LogTypeEnum.DELETE)
    public ApiResponseModel<String> deleteByIds(@RequestBody @NotEmpty(message = "请选择数据") List<String> ids) {
        sysDeptService.deleteByIds(ids);
        return success();
    }

    @Operation(summary = "获取部门树选项")
    @GetMapping("option")
    public ApiResponseModel<List<SysDept>> deptTreeOption() {
        return success(sysDeptService.deptTreeOption());
    }

    @Operation(summary = "导出部门数据")
    @PostMapping("export")
    @Log(description = "批量导出部门", type = LogTypeEnum.EXPORT)
    public void exportExcel(@RequestBody SysDept sysDept) {
        List<SysDeptVO> sysDeptVOS = sysDeptService.exportExcel(sysDept);
        ExcelUtils.export(sysDeptVOS, SysDeptVO.class);
    }
}

