package com.lihua.mapper;

import com.lihua.model.dict.SysDictDataVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  common包下mapper
 *  由于模块引用问题导致common无法调用其他模块查询数据库数据
 *  当common需要查询数据库数据时，可在这里进行查询
 */
public interface CommonMapper {

    // 根据字典类型编码查询对应字典数据
    List<SysDictDataVO> queryByDictTypeCode(@Param("dictTypeCode") String dictTypeCode);
}
