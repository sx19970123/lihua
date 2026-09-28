package com.lihua.excel.merge;

import org.apache.fesod.sheet.metadata.Head;
import org.apache.fesod.sheet.write.handler.SheetWriteHandler;
import org.apache.fesod.sheet.write.handler.context.SheetWriteHandlerContext;
import org.apache.fesod.sheet.write.merge.AbstractMergeStrategy;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户导出单元格合并策略：按用户名分组，分组列（username 等）的连续行合并。
 * 合并区域先收集、sheet 写完后统一提交，且走 addMergedRegionUnsafe 免校验通道——
 * 逐次 addMergedRegion 会全量校验既有区域（区域数大时呈平方级），而本策略的
 * 区域按行分组连续划分、组内列互异，天然不重叠，校验本无必要
 */
public class UserMergeStrategy extends AbstractMergeStrategy implements SheetWriteHandler {

    /**
     * 需要合并的列字段名
     */
    private static final Set<String> MERGE_FIELD_NAMES = Set.of("username", "nickname", "gender", "status", "registerType", "phoneNumber", "email", "roleName", "remark");

    /**
     * 分组字段名（连续相同即同组）
     */
    private static final String GROUP_FIELD_NAME = "username";

    /**
     * 已解析的待合并列坐标（首行各列回调时填充，集合大小凑满后不再判断）
     */
    private final Set<Integer> mergeColumnIndexSet = new HashSet<>();

    /**
     * 收集待提交的合并区域
     */
    private final List<CellRangeAddress> pendingMergeRegions = new ArrayList<>();

    /**
     * 当前分组值与起始行
     */
    private String groupValue;
    private int groupStartRowIndex = -1;

    /**
     * 已处理到的最新行（收尾分组用）
     */
    private int lastRowIndex = -1;

    @Override
    protected void merge(Sheet sheet, Cell cell, Head head, Integer relativeRowIndex) {
        // 首行解析各合并列坐标，凑满后跳过判断
        if (mergeColumnIndexSet.size() != MERGE_FIELD_NAMES.size() && MERGE_FIELD_NAMES.contains(head.getFieldName())) {
            mergeColumnIndexSet.add(head.getColumnIndex());
        }

        // 分组字段所在列的单元格回调时推进分组
        if (GROUP_FIELD_NAME.equals(head.getFieldName())) {
            int rowIndex = relativeRowIndex + head.getHeadNameList().size();
            String username = cell.getStringCellValue();
            if (!username.equals(groupValue)) {
                closeGroup(rowIndex - 1);
                groupValue = username;
                groupStartRowIndex = rowIndex;
            }
            lastRowIndex = rowIndex;
        }
    }

    @Override
    public void afterSheetDispose(SheetWriteHandlerContext context) {
        closeGroup(lastRowIndex);
        Sheet sheet = context.getWriteSheetHolder().getSheet();
        for (CellRangeAddress region : pendingMergeRegions) {
            sheet.addMergedRegionUnsafe(region);
        }
    }

    /**
     * 收尾当前分组：与起始行之间的连续行，在每个待合并列上生成合并区域
     */
    private void closeGroup(int endRowIndex) {
        if (groupStartRowIndex < 0 || groupStartRowIndex >= endRowIndex) {
            return;
        }
        mergeColumnIndexSet.forEach(columnIndex ->
                pendingMergeRegions.add(new CellRangeAddress(groupStartRowIndex, endRowIndex, columnIndex, columnIndex)));
    }
}
