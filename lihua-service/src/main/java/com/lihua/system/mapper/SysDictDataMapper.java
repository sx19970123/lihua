package com.lihua.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lihua.system.entity.SysDictData;

import java.util.List;

public interface SysDictDataMapper extends BaseMapper<SysDictData> {

    // 根据字典类型id集合查询对应的字典数据id
    List<String> selectDataIdsByTypeIds(List<String> ids);
}
