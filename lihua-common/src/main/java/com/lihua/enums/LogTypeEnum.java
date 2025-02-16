package com.lihua.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LogTypeEnum {

    /**
     * 登录
     */
    LOGIN("LOGIN", "登录"),
    /**
     * 注册
     */
    REGISTER("REGISTER", "注册"),
    /**
     * 保存
     */
    SAVE("SAVE","保存数据"),
    /**
     * 保存
     */
    UPDATE_STATUS("UPDATE_STATUS","更新状态"),
    /**
     * 批量导入
     */
    IMPORT("IMPORT","批量导入"),
    /**
     * 批量导出
     */
    EXPORT("EXPORT","批量导出"),
    /**
     * 删除
     */
    DELETE("DELETE","删除数据"),
    /**
     * 列表查询
     */
    QUERY_LIST("QUERY_LIST","列表查询"),
    /**
     * 分页查询
     */
    QUERY_PAGE("QUERY_PAGE","分页查询"),
    /**
     * 单条查询
     */
    QUERY_ONE("QUERY_ONE","单条查询"),
    /**
     * 附件下载
     */
    DOWNLOAD("DOWNLOAD","附件下载"),
    /**
     * 附件上传
     */
    UPLOAD("UPLOAD","附件上传"),
    /**
     * 其他
     */
    OTHER("OTHER","其他");

    private final String code;

    private final String msg;
}
