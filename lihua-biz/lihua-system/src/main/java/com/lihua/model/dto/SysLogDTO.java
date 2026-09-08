package com.lihua.model.dto;

import com.lihua.mybatis.model.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

/**
 * 系统日志查询条件（字段与两日志页检索项对齐）
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysLogDTO extends BaseDTO {
    /**
     * 业务描述
     */
    private String description;

    /**
     * 业务类型
     */
    private String typeCode;

    /**
     * 操作人姓名
     */
    private String createName;

    /**
     * 用户名
     */
    private String username;

    /**
     * 日志执行状态
     */
    private String executeStatus;

    /**
     * 客户端类型
     */
    private String clientType;

    /**
     * 创建时间集合
     */
    private List<LocalDate> createTimeList;
}
