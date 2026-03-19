package com.lihua.controller.app;

import com.lihua.common.model.response.ApiResponseModel;
import com.lihua.common.model.response.basecontroller.ApiResponseController;
import com.lihua.dict.model.DictDataModel;
import com.lihua.service.SysDictDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "APP-字典数据")
@RestController
@RequestMapping("app/system/dictData")
@Validated
public class AppSysDictDataController extends ApiResponseController {

    @Resource
    private SysDictDataService sysDictDataService;

    @Operation(summary = "根据字典类型获取选项")
    @GetMapping("option/{dictTypeCode}")
    public ApiResponseModel<List<DictDataModel>> queryDictOptionList(@PathVariable("dictTypeCode") String dictTypeCode) {
        return success(sysDictDataService.queryDictOptionList(dictTypeCode));
    }

    @Operation(summary = "根据字典类型集合获取选项")
    @PostMapping("option")
    public ApiResponseModel<Map<String, List<DictDataModel>>> queryDictOptionList(@RequestBody List<String> dictTypeCodeList) {
        return success(sysDictDataService.queryDictOptionList(dictTypeCodeList));
    }
}
