package com.lihua.system.controller;

import com.lihua.model.web.BaseController;
import com.lihua.system.entity.SysDept;
import com.lihua.system.model.vo.SysDeptVO;
import com.lihua.system.service.SysDeptService;
import com.lihua.system.service.SysUserDeptService;
import com.lihua.utils.excel.ExcelUtils;
import com.lihua.utils.file.FileDownloadUtils;
import com.lihua.utils.tree.TreeUtils;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import lombok.SneakyThrows;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("system/dept")
public class SysDeptController extends BaseController {

    @Resource
    private SysDeptService sysDeptService;

    @Resource
    private SysUserDeptService sysUserDeptService;

    @PostMapping("list")
    public String findDeptPostList(@RequestBody SysDept sysDept) {
        List<SysDeptVO> deptPostList = sysDeptService.findDeptPostList(sysDept);
        return success(TreeUtils.buildTree(deptPostList));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping
    public String save(@RequestBody @Validated SysDept sysDept) {
        return success(sysDeptService.save(sysDept));
    }

    @GetMapping("{id}")
    public String findById(@PathVariable("id") String id) {
        return success(sysDeptService.findById(id));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("updateStatus/{id}/{currentStatus}")
    public String updateStatus(@PathVariable("id") String id,@PathVariable("currentStatus") String currentStatus) {
        return success(sysDeptService.updateStatus(id, currentStatus));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping
    public String deleteByIds(@RequestBody @NotEmpty(message = "请选择数据") List<String> ids) {
        sysDeptService.deleteByIds(ids);
        return success();
    }

    /**
     * 菜单下拉框选项
     * @return
     */
    @GetMapping("option")
    public String deptTreeOption() {
        return success(sysDeptService.deptTreeOption());
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("export")
    public String exportExcel(@RequestBody SysDept sysDept) {
        String path = sysDeptService.exportExcel(sysDept);
        return success(FileDownloadUtils.addToDownloadableList(path));
    }

    @SneakyThrows
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("import")
    public String importExcel(@RequestParam("file") MultipartFile file) {
        List<SysDeptVO> sysUserVOS = ExcelUtils.importExport(file.getInputStream(), SysDeptVO.class, 0);
        return success(sysDeptService.importExcel(sysUserVOS));
    }
}

