package com.lihua.converter;

import com.lihua.annotation.DictType;
import com.lihua.utils.DictUtils;
import org.apache.fesod.sheet.converters.Converter;
import org.apache.fesod.sheet.metadata.GlobalConfiguration;
import org.apache.fesod.sheet.metadata.data.ReadCellData;
import org.apache.fesod.sheet.metadata.data.WriteCellData;
import org.apache.fesod.sheet.metadata.property.ExcelContentProperty;
import org.springframework.util.StringUtils;

/**
 * 字典转换器
 */
public class DictConverter implements Converter<String> {


    @Override
    public WriteCellData<?> convertToExcelData(String value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        // 字典值不存在
        if (!StringUtils.hasText(value)) {
            return new WriteCellData<>("");
        }

        // 字典注解不存在
        DictType annotation = contentProperty.getField().getAnnotation(DictType.class);
        if (annotation == null) {
            return new WriteCellData<>(value);
        }

        // 获取字典类型编码
        String dictTypeCode = annotation.value();

        // 获取字典 label
        String label = DictUtils.getLabel(dictTypeCode, value);
        if (label == null) {
            label = "";
        }

        return new WriteCellData<>(label);
    }

    @Override
    public String convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return Converter.super.convertToJavaData(cellData, contentProperty, globalConfiguration);
    }
}
