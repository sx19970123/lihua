package com.lihua.utils.excel;

import com.github.liaochong.myexcel.core.DefaultExcelBuilder;
import com.github.liaochong.myexcel.core.DefaultStreamExcelBuilder;
import com.github.liaochong.myexcel.utils.FileExportUtil;
import com.lihua.config.LihuaConfig;
import com.lihua.exception.ServiceException;
import com.lihua.utils.spring.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * excel导入导出工具类
 */
@Slf4j
public class ExcelUtils {

    /**
     * excel 导出
     * @param exportList
     * @param clazz
     * @param fileName
     * @return
     */
    public static <T> File excelExport(List<T> exportList, Class<T> clazz, String fileName) {
        DefaultStreamExcelBuilder<T> excelBuilder = DefaultStreamExcelBuilder
                .of(clazz)
                .autoMerge()
                // 自定义表头样式，详见：https://github.com/liaochong/myexcel/wiki/Style-support
                .style("title->background-color:rgb(217,217,217);vertical-align:center;text-align:center;border-style:thin")
                .titleRowHeight(30)
                .start();
        excelBuilder.append(exportList);
        Workbook workbook = excelBuilder.build();
        File file = new File(handleFullFilePath(fileName));
        try (excelBuilder) {
            FileExportUtil.export(workbook, file);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            throw new ServiceException(e.getMessage());
        }
        return file;
    }


    // 处理全文件地址
    private static String handleFullFilePath(String fileName) {
        if (!StringUtils.hasText(fileName)) {
            throw new ServiceException("导出文件名为空");
        }

        // 去除文件名首部/
        if (fileName.startsWith("/")) {
            fileName = fileName.substring(1);
        }

        // 未指定文件后缀指定为xlsx
        if (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")) {
            fileName = fileName + ".xlsx";
        }

        LihuaConfig lihuaConfig = SpringUtils.getBean(LihuaConfig.class);

        return lihuaConfig.getExportFilePath() + fileName;
    }
}
