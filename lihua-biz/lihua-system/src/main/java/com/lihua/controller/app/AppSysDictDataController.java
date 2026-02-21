package com.lihua.controller.app;

import com.lihua.model.DictDataModel;
import com.lihua.model.web.ApiResponseModel;
import com.lihua.model.web.basecontroller.ApiResponseController;
import com.lihua.service.SysDictDataService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("app/system/dictData")
@Validated
public class AppSysDictDataController extends ApiResponseController {

    @Resource
    private SysDictDataService sysDictDataService;

    @GetMapping("option/{dictTypeCode}")
    public ApiResponseModel<List<DictDataModel>> queryDictOptionList(@PathVariable("dictTypeCode") String dictTypeCode) {
        return success(sysDictDataService.queryDictOptionList(dictTypeCode));
    }

    @PostMapping("option")
    public ApiResponseModel<Map<String, List<DictDataModel>>> queryDictOptionList(@RequestBody List<String> dictTypeCodeList) {
        return success(sysDictDataService.queryDictOptionList(dictTypeCodeList));
    }
}
