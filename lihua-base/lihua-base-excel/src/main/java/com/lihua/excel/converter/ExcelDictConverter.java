package com.lihua.excel.converter;

import com.lihua.common.exception.ServiceException;
import com.lihua.dict.utils.DictUtils;
import com.lihua.excel.annotation.ExcelDictType;
import com.lihua.excel.annotation.ExcelDropdown;
import com.lihua.excel.enums.DropdownTypeEnum;
import org.apache.fesod.sheet.converters.Converter;
import org.apache.fesod.sheet.metadata.GlobalConfiguration;
import org.apache.fesod.sheet.metadata.data.ReadCellData;
import org.apache.fesod.sheet.metadata.data.WriteCellData;
import org.apache.fesod.sheet.metadata.property.ExcelContentProperty;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;

/**
 * 字典转换器
 */
public class ExcelDictConverter implements Converter<String> {

    /**
     * excel导出字典转换器
     * code -> label
     */
    @Override
    public WriteCellData<?> convertToExcelData(String value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        // 字典值不存在
        if (!StringUtils.hasText(value)) {
            return new WriteCellData<>("");
        }

        // 字典注解不存在
        ExcelDictType annotation = contentProperty.getField().getAnnotation(ExcelDictType.class);
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

    /**
     * excel导入字典转换器
     * label -> code
     */
    @Override
    public String convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        // 获取字典类型编码
        String dictTypeCode = getDictTypeCode(contentProperty.getField());

        // 获取字典值
        String dictLabel = cellData.getStringValue();

        String value = DictUtils.getValue(dictTypeCode, dictLabel);

        if (value == null) {
            throw new ServiceException("字典转换失败");
        }

        return value;
    }

    /**
     * 从注解获取字典类型编码
     */
    private String getDictTypeCode(Field field) {
        // 获取 ExcelDictType 注解
        ExcelDictType excelDictType = field.getAnnotation(ExcelDictType.class);

        if (excelDictType == null) {
            // ExcelDictType 注解不存在获取 ExcelDropdown 注解，获取dictCode
            ExcelDropdown excelDropdown = field.getAnnotation(ExcelDropdown.class);
            if (excelDropdown == null) {
                throw new ServiceException("字典相关注解不存在");
            }
            if (excelDropdown.type() != DropdownTypeEnum.DICT) {
                throw new ServiceException("ExcelDropdown 注解类型不为字典类型");
            }
            return excelDropdown.value();
        }

        return excelDictType.value();
    }
}
