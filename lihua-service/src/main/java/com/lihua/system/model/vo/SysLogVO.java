package com.lihua.system.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统日志
 */
@Data
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
    private String description;

    /**
     * 业务类型
     */
    private String type;

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
     * 创建时间（操作时间）
     */
    private LocalDateTime createTime;

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
}
