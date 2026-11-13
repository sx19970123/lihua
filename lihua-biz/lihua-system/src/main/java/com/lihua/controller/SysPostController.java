package com.lihua.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.common.model.response.ResponseModel;
import com.lihua.common.model.response.ResponseController;
import com.lihua.entity.SysPost;
import com.lihua.excel.utils.ExcelUtils;
import com.lihua.log.annotation.Log;
import com.lihua.log.enums.LogTypeEnum;
import com.lihua.model.dto.SysPostDTO;
import com.lihua.model.vo.SysPostVO;
import com.lihua.mybatis.model.validation.MaxPageSizeLimit;
import com.lihua.service.SysPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "岗位管理")
@RestController
@RequestMapping("system/post")
@Validated
public class SysPostController extends ResponseController {

    @Resource
    private SysPostService sysPostService;


    @Operation(summary = "分页查询")
    @PostMapping("page")
    public ResponseModel<IPage<SysPostVO>> queryPage(@RequestBody @Validated(MaxPageSizeLimit.class) SysPostDTO dto) {
        return success(sysPostService.queryPage(dto));
    }

    @Operation(summary = "根据id查询详情")
    @GetMapping("{id}")
    public ResponseModel<SysPost> queryById(@PathVariable("id") String id) {
        return success(sysPostService.queryById(id));
    }

    @Operation(summary = "保存岗位信息")
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping
    @Log(description = "保存岗位信息", type = LogTypeEnum.SAVE)
    public ResponseModel<String> save(@RequestBody @Validated SysPost sysPost) {
        return success(sysPostService.savePost(sysPost));
    }

    @Operation(summary = "更新状态")
    @PreAuthorize("hasRole('ROLE_admin')")
    @PutMapping("status/{id}/{currentStatus}")
    @Log(description = "更新岗位状态", type = LogTypeEnum.UPDATE_STATUS)
    public ResponseModel<String> updateStatus(@PathVariable("id") String id, @PathVariable("currentStatus") String currentStatus) {
        return success(sysPostService.updateStatus(id, currentStatus));
    }

    @Operation(summary = "批量删除")
    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping
    @Log(description = "删除岗位数据", type = LogTypeEnum.DELETE)
    public ResponseModel<String> deleteByIds(@RequestBody @NotEmpty(message = "请选中要删除的数据") List<String> ids) {
        sysPostService.deleteByIds(ids);
        return success();
    }

    @Operation(summary = "获取岗位选项")
    @PostMapping("option")
    public ResponseModel<Map<String, List<SysPost>>> getPostOptionByDeptId(@RequestBody @NotEmpty(message = "部门集合为空") List<String> deptIds) {
        return success(sysPostService.getPostOptionByDeptId(deptIds));
    }

    @Operation(summary = "获取岗位选项（含停用，注册配置页专用）")
    @PostMapping("option/all")
    public ResponseModel<Map<String, List<SysPost>>> getPostOptionAllByDeptId(@RequestBody @NotEmpty(message = "部门集合为空") List<String> deptIds) {
        return success(sysPostService.getPostOptionAllByDeptId(deptIds));
    }

    @Operation(summary = "导出岗位数据")
    @PostMapping("export")
    @Log(description = "导出岗位数据", type = LogTypeEnum.EXPORT)
    public void exportExcel(@RequestBody SysPostDTO dto) {
        List<SysPostVO> sysPostVOS = sysPostService.exportExcel(dto);
        ExcelUtils.export(sysPostVOS, SysPostVO.class);
    }
}
