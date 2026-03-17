package com.lihua.service;

import com.lihua.dict.model.DictDataModel;
import com.lihua.entity.SysDictData;
import com.lihua.model.dto.SysDictDataDTO;

import java.util.List;
import java.util.Map;

public interface SysDictDataService {
    /**
     * 列表查询
     * @return 列表数据
     */
    List<SysDictData> queryList(SysDictDataDTO dictDataDTO);

    /**
     * 获取字典数据option
     */
    List<DictDataModel> queryDictOptionList(String dictTypeCode);

    /**
     * 获取字典数据option
     */
    Map<String, List<DictDataModel>> queryDictOptionList(List<String> dictTypeCodeList);

    /**
     * 保存方法
     * @return 保存数据的主键id
     */
    String save(SysDictData sysDictData);

    /**
     * 字典类型 type 更新时，同时更新字典数据对应的 code
     */
    void updateDataTypeCode(String oldTypeCode,String newTypeCode);

    /**
     * 通过字典类型ids 获取对应的 字典数据ids
     */
    List<String> typeIdsToDataIds(List<String> ids);

    /**
     * 批量删除数据
     */
    void deleteByIds(List<String> ids);
}
