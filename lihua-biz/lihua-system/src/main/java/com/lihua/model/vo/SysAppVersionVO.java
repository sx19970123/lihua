package com.lihua.model.vo;

import com.lihua.entity.SysAppVersion;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * App 版本发布记录 VO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysAppVersionVO extends SysAppVersion {

    /**
     * App 检查更新时实际生效的下载地址。
     * <p>Android：根据 wgt 连续性检查结果，可能指向 wgt_download_url（热更新）或 download_url（整包）；
     * 附件 path 已组装为公开附件下载相对链（App 端补全 baseURL 后访问），外部直链原样返回。
     * <p>iOS：始终等于 download_url（外部跳转链接）。
     */
    private String effectiveDownloadUrl;

    /**
     * App 实际执行的更新方式：apk（整包）/ wgt（热更新）/ link（外部跳转，iOS）
     */
    private String effectiveType;
}
