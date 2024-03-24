package com.lihua.system.service;

import com.lihua.model.dict.SysDictDataVO;
import com.lihua.system.entity.SysDictData;
import com.lihua.system.model.SysDictDataDTO;

import java.util.List;

public interface SysDictDataService {
    /**
     * 列表查询
     * @param dictDataDTO
     * @return 列表数据
     */
    List<SysDictData> findList(SysDictDataDTO dictDataDTO);

    /**
     * 获取字典数据option
     * @param dictTypeCode
     * @return
     */
    List<SysDictDataVO> findDictOptionList(String dictTypeCode);

    /**
     * 保存方法
     * @param sysDictData
     * @return 保存数据的主键id
     */
    String save(SysDictData sysDictData);

    /**
     * 字典类型 type 更新时，同时更新字典数据对应的 code
     * @param oldTypeCode
     * @param newTypeCode
     */
    void updateDataTypeCode(String oldTypeCode,String newTypeCode);

    /**
     * 通过字典类型ids 获取对应的 字典数据ids
     * @param ids
     * @return
     */
    List<String> typeIdsToDataIds(List<String> ids);

    /**
     * 批量删除数据
     * @param ids
     * @return
     */
    void deleteByIds(List<String> ids);
}
