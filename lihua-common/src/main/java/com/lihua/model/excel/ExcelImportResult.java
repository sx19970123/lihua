package com.lihua.model.excel;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * excel 导入返回实体类
 */
@Data
@AllArgsConstructor
public class ExcelImportResult implements Serializable {
    /**
     * 是否全部导入成功
     */
    private boolean allSuccess;

    /**
     * 读取到的数量
     */
    private int readCount;

    /**
     * 导入成功的数量
     */
    private int successCount;

    /**
     * 导入失败的数量
     */
    private int errorCount;

    /**
     * 倒入失败附件地址
     */
    private String errorExcelPath;
}
