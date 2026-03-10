package com.lihua.excel.converter;

import org.apache.fesod.sheet.converters.Converter;
import org.apache.fesod.sheet.metadata.GlobalConfiguration;
import org.apache.fesod.sheet.metadata.data.WriteCellData;
import org.apache.fesod.sheet.metadata.property.ExcelContentProperty;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * LocatDate转换器
 */
public class ExcelLocalDateConverter implements Converter<LocalDate> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 将LocalDate转为 yyyy-MM-dd
     */
    @Override
    public WriteCellData<?> convertToExcelData(LocalDate value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        if (value != null) {
            return new WriteCellData<>(value.format(formatter));
        }
        return new WriteCellData<>("");
    }
}
