package com.lihua.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lihua.mybatis.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * App 版本发布记录
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysAppVersion extends BaseEntity {

    /**
     * 主键 id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 版本名称（与 manifest.json versionName 一致，如 1.2.0）
     */
    private String versionName;

    /**
     * 版本序号（与 manifest.json versionCode 一致，整数，如 10200）
     */
    private Integer versionCode;

    /**
     * 平台（字典 app_version_platform：android/ios）
     */
    private String platform;

    /**
     * 主包地址（android=apk 附件 path 或 HTTP(S) 直链；ios=外部跳转链接）
     */
    private String downloadUrl;

    /**
     * 是否支持 wgt 热更新（0 否 / 1 是，ios 恒为 0）
     */
    private String enableWgt;

    /**
     * 热更新地址（仅 android，enable_wgt=1 时必填附件 path 或 HTTP(S) 直链）
     */
    private String wgtDownloadUrl;

    /**
     * 更新说明（纯文本多行）
     */
    private String updateContent;

    /**
     * 状态（字典 app_version_status：0 草稿 / 1 已发布 / 2 已下线）
     */
    private String status;

    /**
     * 发布时间
     */
    private LocalDateTime publishTime;
}
