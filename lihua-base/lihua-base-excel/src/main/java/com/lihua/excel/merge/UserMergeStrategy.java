package com.lihua.excel.merge;

import org.apache.fesod.sheet.metadata.Head;
import org.apache.fesod.sheet.write.merge.AbstractMergeStrategy;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.HashSet;
import java.util.Set;

/**
 * 处理用户导出单元格合并策略
 */
public final class UserMergeStrategy extends AbstractMergeStrategy {

    /**
     * 导出的总行数
     */
    private final int totalRows;

    /**
     * 需要合并的字段属性
     */
    private final Set<String> mergeProperties = Set.of("username", "nickname", "gender", "status", "registerType", "phoneNumber", "email", "roleName", "remark");

    /**
     * 当前正在扫描的用户名
     */
    private String lastUsername;

    /**
     * 用户名所在的列索引
     */
    private int usernameIndex = -1;

    /**
     * 列总数
     */
    private int totalColumnIndex = -1;

    /**
     * 合并开始行数
     */
    private int mergeStartNumber;

    /**
     * 可合并的列的索引集合
     */
    private final Set<Integer> mergePropertiesIndexSet = new HashSet<>();

    @Override
    protected void merge(Sheet sheet, Cell cell, Head head, Integer relativeRowIndex) {
        // 表头行数
        int headSize = head.getHeadNameList().size();
        // 当前读取到数据的行数，当前rowIndex + 表头行数
        int rowIndex = relativeRowIndex + headSize;
        // 当前读取到的列数
        int columnIndex = head.getColumnIndex();

        // 初始化公共属性
        initMergeProperties(sheet, cell, head);

        // 读取第一行数据
        if (columnIndex + 1 == totalColumnIndex) {
            // 当前一行数据
            Row currentRow = sheet.getRow(rowIndex);
            // 当前行读到的用户名
            String userName = currentRow.getCell(usernameIndex).getStringCellValue();
            // 读取的数据第一行等于表头行数
            if (rowIndex == headSize) {
                lastUsername = userName;
                mergeStartNumber = rowIndex;
            }
            // 读取最后一行数据
            else if (rowIndex == totalRows - 1 + headSize) {
                // 最后一行读取到的username与当前不同，表示需要处理上一个username的合并逻辑，最后一个username只有一个，无需处理
                if (!userName.equals(lastUsername)) {
                    handleMerge(sheet, mergeStartNumber, rowIndex - 1);
                } else {
                    // 相同则需要处理最后一条username的合并
                    handleMerge(sheet, mergeStartNumber, rowIndex);
                }
            }
            // 读取中间行数据
            else {
                if (!userName.equals(lastUsername)) {
                    // rowIndex 为需要合并的下一行行号，需要 rowIndex - 1
                    handleMerge(sheet, mergeStartNumber, rowIndex - 1);

                    lastUsername = userName;
                    mergeStartNumber = rowIndex;
                }
            }
        }
    }

    /**
     * 初始化公共属性
     */
    private void initMergeProperties(Sheet sheet, Cell cell, Head head) {
        // 当前读取到的字段是否需要被合并
        String fieldName = head.getFieldName();

        // 可被合并的列坐标集合，最终集合会与 mergeProperties 集合长度一致，当两集合长度相同时就跳过执行
        if (mergePropertiesIndexSet.size() != mergeProperties.size()) {
            if (mergeProperties.contains(fieldName)) {
                mergePropertiesIndexSet.add(head.getColumnIndex());
            }
        }

        // 唯一标识（用户名）列坐标
        if (usernameIndex == -1 && "username".equals(fieldName)) {
            usernameIndex = cell.getColumnIndex();
        }

        // 获取列总数
        if (totalColumnIndex == -1) {
            Row row = sheet.getRow(0);
            if (row != null) {
                totalColumnIndex = row.getLastCellNum();
            } else {
                totalColumnIndex = 0;
            }
        }

    }

    /**
     * 处理合并单元格
     */
    private void handleMerge(Sheet sheet, int startRowIndex, int endRowIndex) {
        // 开始与结束不能为同一单元格
        if (startRowIndex == endRowIndex) {
            return;
        }

        // 循环可合并列进行合并
        mergePropertiesIndexSet.forEach(index -> sheet.addMergedRegion(new CellRangeAddress(startRowIndex, endRowIndex, index, index)));
    }

    public UserMergeStrategy(int totalRows) {
        this.totalRows = totalRows;
    }
}
