package com.lihua.controller;

import com.lihua.annotation.Log;
import com.lihua.entity.SysDept;
import com.lihua.enums.LogTypeEnum;
import com.lihua.model.vo.SysDeptVO;
import com.lihua.model.response.ApiResponseModel;
import com.lihua.model.response.basecontroller.ApiResponseController;
import com.lihua.service.SysDeptService;
import com.lihua.utils.ExcelUtils;
import com.lihua.utils.tree.TreeUtils;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("system/dept")
@Validated
public class SysDeptController extends ApiResponseController {

    @Resource
    private SysDeptService sysDeptService;

    @PostMapping("list")
    public ApiResponseModel<List<SysDeptVO>> queryDeptPostList(@RequestBody SysDept sysDept) {
        List<SysDeptVO> deptPostList = sysDeptService.queryDeptPostList(sysDept);
        return success(TreeUtils.buildTree(deptPostList));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping
    @Log(description = "保存部门数据", type = LogTypeEnum.SAVE)
    public ApiResponseModel<String> save(@RequestBody @Validated SysDept sysDept) {
        return success(sysDeptService.saveDept(sysDept));
    }

    @GetMapping("{id}")
    public ApiResponseModel<SysDept> queryById(@PathVariable("id") String id) {
        return success(sysDeptService.queryById(id));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("updateStatus/{id}/{currentStatus}")
    @Log(description = "更新部门状态", type = LogTypeEnum.UPDATE_STATUS)
    public ApiResponseModel<String> updateStatus(@PathVariable("id") String id, @PathVariable("currentStatus") String currentStatus) {
        return success(sysDeptService.updateStatus(id, currentStatus));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping
    @Log(description = "删除部门数据", type = LogTypeEnum.DELETE)
    public ApiResponseModel<String> deleteByIds(@RequestBody @NotEmpty(message = "请选择数据") List<String> ids) {
        sysDeptService.deleteByIds(ids);
        return success();
    }

    @GetMapping("option")
    public ApiResponseModel<List<SysDept>> deptTreeOption() {
        return success(sysDeptService.deptTreeOption());
    }

    @PostMapping("export")
    @Log(description = "批量导出部门", type = LogTypeEnum.EXPORT)
    public void exportExcel(@RequestBody SysDept sysDept) {
        List<SysDeptVO> sysDeptVOS = sysDeptService.exportExcel(sysDept);
        ExcelUtils.export(sysDeptVOS, SysDeptVO.class);
    }
}

