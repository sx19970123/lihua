package com.lihua.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lihua.dict.model.DictDataModel;
import com.lihua.entity.SysDictData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysDictDataMapper extends BaseMapper<SysDictData> {

    // 根据字典类型id集合查询对应的字典数据id
    List<String> selectDataIdsByTypeIds(List<String> ids);

    // 字典缓存回源查询：按类型编码批量查询生效字典数据
    List<DictDataModel> queryByDictTypeCode(@Param("dictTypeCodeList") List<String> dictTypeCodeList);
}
