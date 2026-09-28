package com.lihua.excel.converter;

import com.lihua.common.utils.date.DateUtils;
import org.apache.fesod.sheet.converters.Converter;
import org.apache.fesod.sheet.metadata.GlobalConfiguration;
import org.apache.fesod.sheet.metadata.data.WriteCellData;
import org.apache.fesod.sheet.metadata.property.ExcelContentProperty;

import java.time.LocalDate;

/**
 * LocatDate转换器
 */
public class ExcelLocalDateConverter implements Converter<LocalDate> {

    /**
     * 将LocalDate转为 yyyy-MM-dd
     */
    @Override
    public WriteCellData<?> convertToExcelData(LocalDate value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        if (value != null) {
            return new WriteCellData<>(DateUtils.format(value));
        }
        return new WriteCellData<>("");
    }
}
