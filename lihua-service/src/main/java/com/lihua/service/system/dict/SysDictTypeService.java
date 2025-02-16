package com.lihua.service.system.dict;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lihua.entity.system.SysDictType;
import com.lihua.model.system.dto.SysDictTypeDTO;

import java.util.List;

public interface SysDictTypeService {

    /**
     * 列表查询
     * @return 列表数据
     */
    IPage<SysDictType> queryPage(SysDictTypeDTO dictTypeDTO);

    /**
     * 根据id查询数据
     */
    SysDictType queryById(String id);

    /**
     * 保存方法
     * @return 保存数据的主键id
     */
    String save(SysDictType sysDictType);

    /**
     * 批量删除数据
     */
    void deleteByIds(List<String> ids);


    /**
     * 修改状态
     */
    String updateStatus(String id, String currentStatus);

    /**
     * 刷新字典缓存
     */
    void reloadCache();

}
