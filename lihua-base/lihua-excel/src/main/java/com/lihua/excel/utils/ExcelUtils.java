package com.lihua.excel.utils;

import com.lihua.common.exception.ServiceException;
import com.lihua.common.utils.web.WebUtils;
import com.lihua.excel.annotation.ExcelEnableComment;
import com.lihua.excel.annotation.ExcelEnableDropdown;
import com.lihua.excel.handle.CommentHandler;
import com.lihua.excel.handle.DropdownHandler;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.fesod.sheet.FesodSheet;
import org.apache.fesod.sheet.context.AnalysisContext;
import org.apache.fesod.sheet.read.listener.ReadListener;
import org.apache.fesod.sheet.write.builder.ExcelWriterBuilder;
import org.apache.fesod.sheet.write.handler.WriteHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 基于Fesod的excel导入导出工具类
 */
@Slf4j
public class ExcelUtils {

    /**
     * excel 导出
     * @param exportData 需要导出的数据
     * @param clazz 导出的数据类型
     */
    public static void export(Collection<?> exportData, Class<?> clazz) {
        export(exportData, clazz, null);
    }

    /**
     * excel 导出
     * @param exportData 需要导出的数据
     * @param clazz 导出的数据类型
     * @param mergeStrategy 单元格合并策略
     */
    public static <T extends WriteHandler> void export(Collection<?> exportData, Class<?> clazz, T mergeStrategy) {
        try {
            ServletOutputStream outputStream = getExcelResponse().getOutputStream();
            ExcelWriterBuilder write = FesodSheet.write(outputStream, clazz);

            // 合并单元格
            if (mergeStrategy != null) {
                write.registerWriteHandler(mergeStrategy);
            }

            // 判断是否启用了单元格下拉
            ExcelEnableDropdown excelEnableDropdown = clazz.getAnnotation(ExcelEnableDropdown.class);
            if (excelEnableDropdown != null) {
                write.registerWriteHandler(new DropdownHandler());
            }

            // 判断是否启用了单元格批注
            ExcelEnableComment excelEnableComment = clazz.getAnnotation(ExcelEnableComment.class);
            if (excelEnableComment != null) {
                write.inMemory(true);
                write.registerWriteHandler(new CommentHandler());
            }

            write.sheet().doWrite(exportData);

        } catch (IOException e) {
            throw new ServiceException("获取输出流异常");
        }
    }

    /**
     * excel导入
     * @param inputStream excel文件输入流
     * @param clazz 读取的数据类型
     * @return excel中读取到的数据集合
     */
    public static <T> List<T> excelImport(InputStream inputStream, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        FesodSheet.read(inputStream, clazz, new ReadListener<T>() {
                    @Override
                    public void invoke(T data, AnalysisContext context) {
                        list.add(data);
                    }

                    @Override
                    public void doAfterAllAnalysed(AnalysisContext context) {
                        log.info("数据全部读取完成");
                    }
                })
                .sheet()
                .doRead();
        return list;
    }

    /**
     * 获取excel响应对象
     * @return HttpServletResponse
     */
    private static HttpServletResponse getExcelResponse() {
        // 处理响应信息
        HttpServletResponse response = WebUtils.getCurrentResponse();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment");
        return response;
    }
}
