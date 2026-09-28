package com.lihua.dict.loader;

import com.lihua.dict.model.DictDataModel;

import java.util.List;

/**
 * 字典缓存回源通道：接口属于字典域（消费者），回源查询实现由持有 sys_dict_data 表的服务（system）提供
 */
public interface DictDataLoader {

    /**
     * 按字典类型编码批量查询生效字典数据
     */
    List<DictDataModel> queryByDictTypeCode(List<String> dictTypeCodeList);
}
