package com.lihua.excel.handle;

import com.lihua.dict.model.DictDataModel;
import com.lihua.dict.utils.DictUtils;
import com.lihua.excel.annotation.ExcelDropdown;
import com.lihua.excel.enums.DropdownTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.fesod.sheet.metadata.Head;
import org.apache.fesod.sheet.metadata.data.WriteCellData;
import org.apache.fesod.sheet.write.handler.CellWriteHandler;
import org.apache.fesod.sheet.write.metadata.holder.WriteSheetHolder;
import org.apache.fesod.sheet.write.metadata.holder.WriteTableHolder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 单元格下拉处理器
 */
@Slf4j
public class DropdownHandler implements CellWriteHandler {

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        // 设置下拉仅读取第一行即可
        if (relativeRowIndex > 0) {
            return;
        }
        // 获取下拉注解
        Field field = head.getField();
        field.setAccessible(true);
        ExcelDropdown annotation = field.getAnnotation(ExcelDropdown.class);
        if (annotation == null) {
            return;
        }

        // 获取下拉列表
        String[] options = getOptions(annotation);

        // 设置下拉应用的单元格
        CellRangeAddressList range = new CellRangeAddressList(head.getHeadNameList().size(), annotation.max(), head.getColumnIndex(), head.getColumnIndex());

        // 设置下拉
        Sheet sheet = writeSheetHolder.getSheet();
        DataValidationHelper helper = sheet.getDataValidationHelper();
        DataValidationConstraint explicitListConstraint = helper.createExplicitListConstraint(options);
        DataValidation validation = helper.createValidation(explicitListConstraint, range);
        sheet.addValidationData(validation);
    }

    /**
     * 获取下拉选项
     */
    private String[] getOptions(ExcelDropdown annotation) {
        // 根据不同的下拉类型获取对应label数组
        DropdownTypeEnum dropdownType = annotation.type();
        switch (dropdownType){
            // 字典下拉
            case DICT -> {
                return getDictOptions(annotation);
            }
            // 自定义下拉
            case CUSTOM ->  {
                return getCustomOptions(annotation);
            }
            default -> {
                log.warn("未知的下拉类型，返回空数组");
                return new String[0];
            }
        }
    }

    /**
     * 获取字典下拉
     */
    private String[] getDictOptions(ExcelDropdown annotation) {
        String dictTypeCode = annotation.value();
        List<DictDataModel> dictData = DictUtils.getDictData(dictTypeCode);
        if (dictData.isEmpty()) {
            return new String[0];
        }
        return dictData.stream().map(DictDataModel::getLabel).toArray(String[]::new);
    }

    /**
     * 获取自定义下拉
     */
    private String[] getCustomOptions(ExcelDropdown annotation) {
        return annotation.options();
    }
}
