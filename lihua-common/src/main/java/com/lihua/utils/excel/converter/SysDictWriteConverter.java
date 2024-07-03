package com.lihua.utils.excel.converter;

import com.github.liaochong.myexcel.core.converter.CustomWriteContext;
import com.github.liaochong.myexcel.core.converter.CustomWriteConverter;
import com.lihua.utils.dict.DictUtils;
import com.lihua.utils.excel.annotation.ExcelWriteConverterDictTypeCode;
import org.springframework.util.StringUtils;

public class SysDictWriteConverter implements CustomWriteConverter<String, String> {

    @Override
    public String convert(String dictValue, CustomWriteContext customWriteContext) {

        // 获取自定义注解
        ExcelWriteConverterDictTypeCode annotation = customWriteContext.getField().getAnnotation(ExcelWriteConverterDictTypeCode.class);

        if (annotation == null) {
            return null;
        }

        // 通过注解参数获取字典类型编码
        String dictTypeCode = annotation.value();

        // 通过字典类型编码和字典value获取label
        if (StringUtils.hasText(dictTypeCode)) {
            String label = DictUtils.getLabel(dictTypeCode, dictValue);

            if (StringUtils.hasText(label)) {
                return label;
            }

            return dictValue;
        }

        return dictValue;
    }
}
