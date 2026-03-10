package com.lihua.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lihua.excel.annotation.ExcelDictType;
import com.lihua.excel.converter.ExcelDictConverter;
import com.lihua.excel.converter.ExcelLocalDateTimeConverter;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.fesod.sheet.annotation.ExcelIgnoreUnannotated;
import org.apache.fesod.sheet.annotation.ExcelProperty;
import org.apache.fesod.sheet.annotation.write.style.ColumnWidth;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统日志
 */
@ExcelIgnoreUnannotated
@Data
@Accessors(chain = true)
public class SysLogVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 日志主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 业务描述
     */
    @ExcelProperty("业务描述")
    @ColumnWidth(20)
    private String description;

    /**
     * 业务类型编码
     */
    private String typeCode;

    /**
     * 业务类型描述
     */
    @ExcelProperty("业务类型")
    @ColumnWidth(20)
    private String typeMsg;

    /**
     * 日志执行状态
     */
    @ExcelProperty(value = "执行结果", converter = ExcelDictConverter.class)
    @ColumnWidth(20)
    @ExcelDictType("sys_log_status")
    private String executeStatus;

    /**
     * 包类名
     */
    @ExcelProperty("包类名")
    @ColumnWidth(40)
    private String className;

    /**
     * 方法名
     */
    @ExcelProperty("方法名")
    @ColumnWidth(20)
    private String methodName;

    /**
     * 请求参数
     */
    @ExcelProperty("请求参数")
    @ColumnWidth(30)
    private String params;

    /**
     * 请求返回
     */
    @ExcelProperty("返回结果")
    @ColumnWidth(30)
    private String result;

    /**
     * 创建人（操作人）
     */
    private String createId;

    /**
     * 操作人username
     */
    private String username;

    /**
     * 操作人姓名
     */
    @ExcelProperty("操作人")
    @ColumnWidth(15)
    private String createName;

    /**
     * 创建时间（操作时间）
     */
    @ExcelProperty(value = "操作时间", converter = ExcelLocalDateTimeConverter.class)
    @ColumnWidth(25)
    private LocalDateTime createTime;

    /**
     * 执行耗时
     */
    @ExcelProperty("耗时(ms)")
    @ColumnWidth(15)
    private Long executeTime;

    /**
     * 请求URL
     */
    @ExcelProperty("请求URL")
    @ColumnWidth(20)
    private String url;

    /**
     * ip地址
     */
    @ExcelProperty("请求IP")
    @ColumnWidth(30)
    private String ipAddress;

    /**
     * 归属地
     */
    @ExcelProperty("归属地")
    @ColumnWidth(20)
    private String region;

    /**
     * 用户代理字符串
     */
    private String userAgent;

    /**
     * 操作客户端
     */
    @ExcelProperty(value = "客户端类型", converter = ExcelDictConverter.class)
    @ColumnWidth(20)
    @ExcelDictType("sys_client_type")
    private String clientType;

    /**
     * 异常描述
     */
    private String errorMsg;

    /**
     * 异常堆栈信息
     */
    private String errorStack;


    /**
     * 删除标识
     */
    private String delFlag;

    /**
     * 缓存key
     */
    private String cacheKey;
}
