package com.lihua.common.model.bridge.log;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 日志跨模块调用传参
 */
@Data
@Accessors(chain = true)
public class LogModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 业务描述
     */
    private String description;

    /**
     * 业务类型编码
     */
    private String typeCode;

    /**
     * 业务类型描述
     */
    private String typeMsg;

    /**
     * 日志执行状态
     */
    private String executeStatus;

    /**
     * 包类名
     */
    private String className;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 请求参数
     */
    private String params;

    /**
     * 请求返回
     */
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
    private String createName;

    /**
     * 创建时间（操作时间）
     */
    private LocalDateTime createTime;

    /**
     * 执行耗时
     */
    private Long executeTime;

    /**
     * 请求URL
     */
    private String url;

    /**
     * ip地址
     */
    private String ipAddress;

    /**
     * 归属地
     */
    private String region;

    /**
     * 用户代理字符串
     */
    private String userAgent;

    /**
     * 操作客户端
     */
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
