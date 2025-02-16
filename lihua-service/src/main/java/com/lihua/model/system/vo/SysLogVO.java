package com.lihua.model.system.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.github.liaochong.myexcel.core.annotation.ExcelColumn;
import com.lihua.utils.excel.annotation.ExcelWriteConverterDictTypeCode;
import com.lihua.utils.excel.converter.SysDictWriteConverter;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统日志
 */
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
    @ExcelColumn(index = 0, title = "业务描述", width = 8)
    private String description;

    /**
     * 业务类型编码
     */
    private String typeCode;

    /**
     * 业务类型描述
     */
    @ExcelColumn(order = 1, index = 1, title = "业务类型", width = 6)
    private String typeMsg;

    /**
     * 日志执行状态
     */
    @ExcelColumn(order = 2, index = 2, title = "执行结果", width = 4, writeConverter = SysDictWriteConverter.class)
    @ExcelWriteConverterDictTypeCode("sys_log_status")
    private String executeStatus;

    /**
     * 包类名
     */
    @ExcelColumn(order = 3, index = 3, title = "类名", width = 16)
    private String className;

    /**
     * 方法名
     */
    @ExcelColumn(order = 4, index = 4, title = "方法名", width = 10)
    private String methodName;

    /**
     * 请求参数
     */
    @ExcelColumn(order = 5, index = 5, title = "参数", width = 20)
    private String params;

    /**
     * 请求返回
     */
    @ExcelColumn(order = 6, index = 6, title = "返回值", width = 20)
    private String result;

    /**
     * 创建人（操作人）
     */
    private String createId;

    /**
     * 操作人username
     */
    @ExcelColumn(order = 7, index = 7, title = "用户名", width = 6)
    private String username;

    /**
     * 操作人姓名
     */
    @ExcelColumn(order = 8, index = 8, title = "操作人", width = 6)
    private String createName;

    /**
     * 创建时间（操作时间）
     */
    @ExcelColumn(order = 9, index = 9, title = "操作时间", width = 10, format="YYYY-MM-DD HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 执行耗时
     */
    @ExcelColumn(order = 10, index = 10, title = "执行时长（ms）", width = 6)
    private Long executeTime;

    /**
     * 请求URL
     */
    @ExcelColumn(order = 11, index = 11, title = "请求地址", width = 20)
    private String url;

    /**
     * ip地址
     */
    @ExcelColumn(order = 12, index = 12, title = "用户ip", width = 8)
    private String ipAddress;

    /**
     * 用户代理字符串
     */
    @ExcelColumn(order = 13, index = 13, title = "操作环境", width = 20)
    private String userAgent;

    /**
     * 异常描述
     */
    @ExcelColumn(order = 14, index = 15, title = "异常信息", width = 20)
    private String errorMsg;

    /**
     * 异常堆栈信息
     */
    @ExcelColumn(order = 15, index = 15, title = "异常堆栈", width = 20)
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
