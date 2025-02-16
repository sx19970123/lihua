package com.lihua.controller.system;

import com.lihua.annotation.Log;
import com.lihua.enums.LogTypeEnum;
import com.lihua.enums.ResultCodeEnum;
import com.lihua.model.web.BaseController;
import com.lihua.entity.system.SysDictData;
import com.lihua.model.system.dto.SysDictDataDTO;
import com.lihua.service.system.dict.SysDictDataService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("system/dictData")
@Validated
public class SysDictDataController extends BaseController {

    @Resource
    private SysDictDataService sysDictDataService;

    /**
     * 查询字典数据列表
     */
    @PostMapping("list")
    public String queryListByTypeCode(@RequestBody SysDictDataDTO dictDataDTO) {
        if (!StringUtils.hasText(dictDataDTO.getDictTypeCode())) {
            return error(ResultCodeEnum.ERROR,"数据字典类型id为空");
        }
        return success(sysDictDataService.queryList(dictDataDTO));
    }

    /**
     * 查询下拉框中字典选项
     */
    @GetMapping("option/{dictTypeCode}")
    public String queryDictOptionList(@PathVariable("dictTypeCode") String dictTypeCode) {
        return success(sysDictDataService.queryDictOptionList(dictTypeCode));
    }

    @PostMapping("option")
    public String queryDictOptionList(@RequestBody List<String> dictTypeCodeList) {
        return success(sysDictDataService.queryDictOptionList(dictTypeCodeList));
    }

    /**
     * 保存字典数据
     */
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping
    @Log(description = "保存字典数据", type = LogTypeEnum.SAVE)
    public String save(@RequestBody @Validated SysDictData sysDictData) {
        return success(sysDictDataService.save(sysDictData));
    }

    /**
     * 删除字典数据
     */
    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping
    @Log(description = "删除字典数据", type = LogTypeEnum.DELETE)
    public String delete(@RequestBody @NotEmpty(message = "请选择字段数据") List<String> ids) {
        sysDictDataService.deleteByIds(ids);
        return success();
    }
}
