package com.lihua.model.excel;

import java.io.Serializable;

/**
 * excel 导入返回实体类
 */
public class ExcelImportResult implements Serializable {

    /**
     * 读取到的数量
     */
    private long readCount;

    /**
     * 导入成功的数量
     */
    private long successCount;

    /**
     * 导入失败的数量
     */
    private long errorCount;
}
