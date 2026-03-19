package com.lihua.excel.handle;

import com.lihua.excel.annotation.ExcelComment;
import com.lihua.excel.enums.CommentUseEnum;
import org.apache.fesod.sheet.metadata.Head;
import org.apache.fesod.sheet.metadata.data.WriteCellData;
import org.apache.fesod.sheet.write.handler.CellWriteHandler;
import org.apache.fesod.sheet.write.metadata.holder.WriteSheetHolder;
import org.apache.fesod.sheet.write.metadata.holder.WriteTableHolder;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 单元格批注处理器
 */
public class CommentHandler implements CellWriteHandler {

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        Field field = head.getField();
        ExcelComment annotation = field.getAnnotation(ExcelComment.class);

        // 检查是否添加批注
        if (!checkAddComment(annotation, cell, relativeRowIndex, isHead)) {
            return;
        }

        Sheet sheet = cell.getSheet();
        Drawing<?> drawing = sheet.createDrawingPatriarch();

        // 批注位置
        XSSFClientAnchor anchor = new XSSFClientAnchor();
        anchor.setCol1(cell.getColumnIndex());
        anchor.setCol2(cell.getColumnIndex() + 2);
        anchor.setRow1(cell.getRowIndex());
        anchor.setRow2(cell.getRowIndex() + 3);

        // 设置批注
        org.apache.poi.ss.usermodel.Comment comment = drawing.createCellComment(anchor);
        comment.setString(new XSSFRichTextString(annotation.value()));
        cell.setCellComment(comment);
    }

    /**
     * 检查是否提添加批注
     */
    private boolean checkAddComment(ExcelComment annotation, Cell cell, int relativeRowIndex, Boolean isHead) {

        if (annotation == null) {
            return false;
        }

        CommentUseEnum use = annotation.use();

        // 应用表头
        if (use == CommentUseEnum.HEAD) {
            if (!isHead) {
                return false;
            }

            return relativeRowIndex == annotation.headRowNum();
        }

        // 应用全部
        if (use == CommentUseEnum.ALL) {
            if (isHead) {
                return relativeRowIndex == annotation.headRowNum();
            }
        }

        // 应用内容
        if (use == CommentUseEnum.BODY) {
            return !isHead;
        }

        return true;
    }
}
