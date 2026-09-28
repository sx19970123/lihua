package com.lihua.excel.converter;

import com.lihua.common.utils.date.DateUtils;
import org.apache.fesod.sheet.converters.Converter;
import org.apache.fesod.sheet.metadata.GlobalConfiguration;
import org.apache.fesod.sheet.metadata.data.WriteCellData;
import org.apache.fesod.sheet.metadata.property.ExcelContentProperty;

import java.time.LocalDateTime;

/**
 * LocatDateTime转换器
 */
public class ExcelLocalDateTimeConverter implements Converter<LocalDateTime> {

    /**
     * 将LocalDateTime转为 yyyy-MM-dd HH:mm:ss
     */
    @Override
    public WriteCellData<?> convertToExcelData(LocalDateTime value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        if (value != null) {
            return new WriteCellData<>(DateUtils.format(value));
        }
        return new WriteCellData<>("");
    }
}
