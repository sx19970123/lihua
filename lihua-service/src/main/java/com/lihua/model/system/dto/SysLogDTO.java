package com.lihua.model.system.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lihua.model.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

/**
 * 系统日志
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysLogDTO extends BaseDTO {
    /**
     * 日志主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 业务描述
     */
    private String description;

    /**
     * 业务类型
     */
    private String typeCode;

    /**
     * 包类名
     */
    private String className;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * ip地址
     */
    private String ipAddress;

    /**
     * 创建人（操作人）
     */
    private String createId;

    /**
     * 操作人姓名
     */
    private String createName;

    /**
     * 用户名
     */
    private String username;

    /**
     * 创建时间集合
     */
    private List<LocalDate> createTimeList;

    /**
     * 执行耗时
     */
    private Long executeTime;

    /**
     * 请求参数
     */
    private String params;

    /**
     * 请求返回
     */
    private String result;

    /**
     * 异常描述
     */
    private String errorMsg;

    /**
     * 请求URL
     */
    private String url;

    /**
     * 用户代理字符串
     */
    private String userAgent;

    /**
     * 删除标识
     */
    private String delFlag;

    /**
     * 日志执行状态
     */
    private String executeStatus;
}
