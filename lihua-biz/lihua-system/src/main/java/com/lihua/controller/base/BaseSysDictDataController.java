package com.lihua.controller.base;

import com.lihua.common.model.response.ApiResponseModel;
import com.lihua.common.model.response.basecontroller.ApiResponseController;
import com.lihua.dict.model.DictDataModel;
import com.lihua.service.SysDictDataService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * 字典数据控制器基类：承载管理版与 App 版共有端点（对外行为双版本一致，差异仅在路由前缀与文档分组）；
 * 字典数据的维护端点（列表/保存/删除）仅管理版提供，由管理版子类自行声明
 */
public abstract class BaseSysDictDataController extends ApiResponseController {

    @Resource
    protected SysDictDataService sysDictDataService;

    @Operation(summary = "根据字典类型批量获取选项")
    @PostMapping("option")
    public ApiResponseModel<Map<String, List<DictDataModel>>> queryDictOptionList(@RequestBody List<String> dictTypeCodeList) {
        return success(sysDictDataService.queryDictOptionList(dictTypeCodeList));
    }
}
