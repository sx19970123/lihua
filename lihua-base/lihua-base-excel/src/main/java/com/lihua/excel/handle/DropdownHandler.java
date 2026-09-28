package com.lihua.excel.handle;

import com.lihua.dict.model.DictDataModel;
import com.lihua.dict.utils.DictUtils;
import com.lihua.excel.annotation.ExcelDropdown;
import com.lihua.excel.enums.DropdownTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.fesod.sheet.metadata.Head;
import org.apache.fesod.sheet.write.handler.SheetWriteHandler;
import org.apache.fesod.sheet.write.metadata.holder.WriteSheetHolder;
import org.apache.fesod.sheet.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.CellReference;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * 单元格下拉处理器（sheet 创建后按表头字段一次性注册全部下拉校验）
 */
@Slf4j
public class DropdownHandler implements SheetWriteHandler {

    /**
     * excel 显式列表约束的字面量长度上限（Excel 规范 255），超出改用隐藏 sheet 区域引用提供选项
     */
    private static final int EXPLICIT_LIST_MAX_LENGTH = 255;

    /**
     * 超长选项的隐藏数据 sheet 名
     */
    private static final String HIDDEN_SHEET_NAME = "dropdown_data";

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        Map<Integer, Head> headMap = writeSheetHolder.excelWriteHeadProperty().getHeadMap();
        // 下拉应用区域自数据首行（表头行数）开始
        int headRowNumber = writeSheetHolder.excelWriteHeadProperty().getHeadRowNumber();
        Sheet sheet = writeSheetHolder.getSheet();

        headMap.forEach((columnIndex, head) -> {
            Field field = head.getField();
            if (field == null) {
                return;
            }
            ExcelDropdown annotation = field.getAnnotation(ExcelDropdown.class);
            if (annotation == null) {
                return;
            }

            String[] options = getOptions(annotation);
            CellRangeAddressList range = new CellRangeAddressList(headRowNumber, annotation.max(), columnIndex, columnIndex);
            DataValidationHelper helper = sheet.getDataValidationHelper();
            DataValidation validation = helper.createValidation(resolveConstraint(writeWorkbookHolder, helper, options), range);
            sheet.addValidationData(validation);
        });
    }

    /**
     * 选项总长在显式列表上限内直接内联；超限写入隐藏 sheet，以区域引用方式提供
     */
    private DataValidationConstraint resolveConstraint(WriteWorkbookHolder writeWorkbookHolder, DataValidationHelper helper, String[] options) {
        if (String.join(",", options).length() + 2 <= EXPLICIT_LIST_MAX_LENGTH) {
            return helper.createExplicitListConstraint(options);
        }

        Sheet hiddenSheet = getOrCreateHiddenSheet(writeWorkbookHolder.getWorkbook());
        int columnIndex = hiddenSheet.getRow(0) == null ? 0 : Math.max(hiddenSheet.getRow(0).getLastCellNum(), 0);
        for (int i = 0; i < options.length; i++) {
            Row row = hiddenSheet.getRow(i);
            if (row == null) {
                row = hiddenSheet.createRow(i);
            }
            row.createCell(columnIndex).setCellValue(options[i]);
        }
        String columnLetter = CellReference.convertNumToColString(columnIndex);
        return helper.createFormulaListConstraint(HIDDEN_SHEET_NAME + "!$" + columnLetter + "$1:$" + columnLetter + "$" + options.length);
    }

    /**
     * 获取或创建隐藏数据 sheet
     */
    private Sheet getOrCreateHiddenSheet(Workbook workbook) {
        Sheet hiddenSheet = workbook.getSheet(HIDDEN_SHEET_NAME);
        if (hiddenSheet == null) {
            hiddenSheet = workbook.createSheet(HIDDEN_SHEET_NAME);
            workbook.setSheetHidden(workbook.getSheetIndex(hiddenSheet), true);
        }
        return hiddenSheet;
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
