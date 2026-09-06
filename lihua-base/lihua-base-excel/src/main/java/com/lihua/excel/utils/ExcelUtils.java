package com.lihua.excel.utils;

import com.lihua.common.exception.ServiceException;
import com.lihua.excel.exception.ExcelExportException;
import com.lihua.excel.handle.CommentHandler;
import com.lihua.excel.handle.DropdownHandler;
import com.lihua.web.utils.WebUtils;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 基于Fesod的excel导入导出工具类
 */
@Slf4j
public class ExcelUtils {

    /**
     * excel 导出（纯数据：流式写出，不携带批注与下拉）
     * @param exportData 需要导出的数据
     * @param clazz 导出的数据类型
     */
    public static void export(Collection<?> exportData, Class<?> clazz) {
        export(exportData, clazz, new WriteHandler[0]);
    }

    /**
     * excel 导出（纯数据：流式写出，不携带批注与下拉）
     * @param exportData 需要导出的数据
     * @param clazz 导出的数据类型
     * @param writeHandlers 写出处理器（如单元格合并策略），按序注册、可传多个
     */
    public static void export(Collection<?> exportData, Class<?> clazz, WriteHandler... writeHandlers) {
        ServletOutputStream outputStream = getOutputStream("export");
        ExcelWriterBuilder write = FesodSheet.write(outputStream, clazz);

        // 注册写出处理器（合并策略等）
        if (writeHandlers != null) {
            for (WriteHandler writeHandler : writeHandlers) {
                write.registerWriteHandler(writeHandler);
            }
        }

        try {
            write.sheet().doWrite(exportData);
        } catch (Exception e) {
            throw new ServiceException("excel 写出异常：" + e.getMessage());
        }
    }

    /**
     * excel 导入模板导出（仅表头行，携带字段批注与下拉作为填表指引；
     * 处理器对无注解字段自动跳过，POI 批注要求全内存写出）
     * @param clazz 模板的数据类型
     */
    public static void exportTemplate(Class<?> clazz) {
        ServletOutputStream outputStream = getOutputStream("template");
        ExcelWriterBuilder write = FesodSheet.write(outputStream, clazz);
        write.inMemory(true);
        write.registerWriteHandler(new DropdownHandler());
        write.registerWriteHandler(new CommentHandler());
        try {
            write.sheet().doWrite(new ArrayList<>());
        } catch (Exception e) {
            throw new ServiceException("excel 写出异常：" + e.getMessage());
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
     * 获取 excel 响应输出流
     * @param fileName 浏览器直接访问接口时的兜底文件名（前端 blob 下载自行命名，不消费此值）
     */
    private static ServletOutputStream getOutputStream(String fileName) {
        // 处理响应信息
        HttpServletResponse response = WebUtils.getCurrentResponse();
        if (response == null) {
            throw new ExcelExportException("获取响应流异常");
        }
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''"
                + URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20") + ".xlsx");
        try {
            return response.getOutputStream();
        } catch (IOException e) {
            throw new ServiceException("获取输出流异常");
        }
    }
}
