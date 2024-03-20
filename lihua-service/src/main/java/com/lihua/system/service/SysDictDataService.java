package com.lihua.system.service;

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
    List<SysDictData> findDictOptionList(String dictTypeCode);

    /**
     * 保存方法
     * @param sysDictData
     * @return 保存数据的主键id
     */
    String save(SysDictData sysDictData);

    /**
     * 批量删除数据
     * @param ids
     * @return
     */
    void deleteByIds(List<String> ids);

    /**
     * 重置缓存
     * @param dictTypeCode
     */
    void resetCache(String dictTypeCode);
}
