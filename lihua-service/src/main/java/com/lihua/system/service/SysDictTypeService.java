package com.lihua.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.system.entity.SysDictType;
import com.lihua.system.model.SysDictTypeDTO;

import java.util.List;

public interface SysDictTypeService {

    /**
     * 列表查询
     * @param dictTypeDTO
     * @return 列表数据
     */
    IPage<SysDictType> findPage(SysDictTypeDTO dictTypeDTO);

    /**
     * 根据id查询数据
     * @param id
     * @return
     */
    SysDictType findById(String id);

    /**
     * 保存方法
     * @param sysDictType
     * @return 保存数据的主键id
     */
    String save(SysDictType sysDictType);

    /**
     * 批量删除数据
     * @param ids
     * @return
     */
    void deleteByIds(List<String> ids);


}
