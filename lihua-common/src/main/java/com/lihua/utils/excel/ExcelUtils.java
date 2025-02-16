package com.lihua.utils.excel;

import com.github.liaochong.myexcel.core.DefaultStreamExcelBuilder;
import com.github.liaochong.myexcel.core.SaxExcelReader;
import com.github.liaochong.myexcel.utils.FileExportUtil;
import com.lihua.config.LihuaConfig;
import com.lihua.exception.FileException;
import com.lihua.exception.ServiceException;
import com.lihua.utils.spring.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * excel导入导出工具类
 */
@Slf4j
public class ExcelUtils {

    /**
     * excel 导出
     * @param exportList 需要导出附件的数据集合
     * @param clazz 对应实体类class
     * @param fileName 导出附件名称
     * @return 生成excel附件的绝对路径
     */
    public static <T> String excelExport(List<T> exportList, Class<T> clazz, String fileName) {
        // 创建 Workbook
        DefaultStreamExcelBuilder<T> excelBuilder = DefaultStreamExcelBuilder
                .of(clazz)
                .autoMerge()
                // 自定义样式，详见：https://github.com/liaochong/myexcel/wiki/Style-support
                .style("cell->border-style:thin;text-align:center;vertical-align:center",
                        "title->background-color:rgb(217,217,217);vertical-align:center;text-align:center;border-style:thin")
                .titleRowHeight(30)
                .start();
        excelBuilder.append(exportList);
        Workbook workbook = excelBuilder.build();

        File file = new File(handleFullFilePath(fileName));

        // 判断父级目录是否存在，不存在的情况下自动创建
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (excelBuilder) {
            // 附件导出
            FileExportUtil.export(workbook, file);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            throw new FileException(e.getMessage());
        }

        return file.getAbsolutePath();
    }

    /**
     * excel 导入
     * @param multipartFile 上传的excel附件
     * @param clazz 接收附件类型class
     * @param rowNum 从0开始数，对应第一行，1对应第二行，程序中设置未大于 rowNum
     *               即设置为 0 的话，从表格中第二行开始读取
     *               设置为 1 的话，从表格中第三行开始读取
     *               根据表头层级数量进行指定，即表头层级数 - 1
     * @return 导入数据集合
     */
    public static <T> List<T> importExport(MultipartFile multipartFile, Class<T> clazz, int rowNum) {
        // 判断上传的附件类型
        String fileName = multipartFile.getOriginalFilename();
        if (fileName != null) {
            fileName = fileName.toLowerCase();
            if (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")) {
                throw new ServiceException("请上传 .xls 或 .xlsx 类型的附件");
            }
        }

        try {
            return SaxExcelReader.of(clazz)
                    .rowFilter(row -> row.getRowNum() > rowNum)
                    .detectedMerge()
                    .read(multipartFile.getInputStream());
        } catch (IOException e) {
            throw new ServiceException("Excel上传异常，获取InputStream失败");
        }
    }


    // 处理全附件地址
    private static String handleFullFilePath(String fileName) {
        if (!StringUtils.hasText(fileName)) {
            throw new ServiceException("导出附件名为空");
        }

        // 去除附件名首部/
        if (fileName.startsWith("/")) {
            fileName = fileName.substring(1);
        }
        // 附件名后拼接uuid
        fileName = fileName + "_" + UUID.randomUUID().toString().replace("-","");
        // 未指定附件后缀指定为xlsx
        if (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")) {
            fileName = fileName + ".xlsx";
        }

        LihuaConfig lihuaConfig = SpringUtils.getBean(LihuaConfig.class);

        return lihuaConfig.getExportFilePath() + fileName;
    }
}
