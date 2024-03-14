package com.lihua.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
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
     * 根据id查询数据
     * @param id
     * @return
     */
    SysDictData findById(String id);

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
}
