package com.lihua.utils;

import com.lihua.exception.ServiceException;
import com.lihua.utils.web.WebUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.fesod.sheet.FesodSheet;
import org.apache.fesod.sheet.write.builder.ExcelWriterBuilder;
import org.apache.fesod.sheet.write.merge.AbstractMergeStrategy;

import java.io.IOException;
import java.util.Collection;

/**
 * 基于Fesod的excel导入导出工具类
 */
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
    public static <T extends AbstractMergeStrategy> void export(Collection<?> exportData, Class<?> clazz, T mergeStrategy) {
        try {
            ServletOutputStream outputStream = getExcelResponse().getOutputStream();
            ExcelWriterBuilder write = FesodSheet.write(outputStream, clazz);

            // 合并单元格
            if (mergeStrategy != null) {
                write.registerWriteHandler(mergeStrategy);
            }

            write.sheet().doWrite(exportData);

        } catch (IOException e) {
            throw new ServiceException("获取输出流异常");
        }
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
