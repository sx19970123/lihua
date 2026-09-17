package com.lihua.loader;

import com.lihua.dict.loader.DictDataLoader;
import com.lihua.dict.model.DictDataModel;
import com.lihua.mapper.SysDictDataMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 字典缓存回源实现：sys_dict_data 表 Owner 提供，@ComponentScan 自动注册为 DictUtils 回源通道
 */
@Component
public class DictDataLoaderImpl implements DictDataLoader {

    @Resource
    private SysDictDataMapper sysDictDataMapper;

    @Override
    public List<DictDataModel> queryByDictTypeCode(List<String> dictTypeCodeList) {
        return sysDictDataMapper.queryByDictTypeCode(dictTypeCodeList);
    }
}
