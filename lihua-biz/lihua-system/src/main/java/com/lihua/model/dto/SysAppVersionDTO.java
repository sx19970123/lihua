package com.lihua.model.dto;

import com.lihua.enums.AppPlatformEnum;
import com.lihua.mybatis.model.BaseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * App 版本发布记录 DTO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysAppVersionDTO extends BaseDTO {

    /**
     * 主键 id（编辑时传入）
     */
    private String id;

    /**
     * 版本名称（与 manifest.json versionName 一致，如 1.2.0）
     */
    @NotBlank(message = "请输入版本名称")
    @Size(max = 20, message = "版本名称长度不能超过20个字符")
    private String versionName;

    /**
     * 版本序号（与 manifest.json versionCode 一致，整数）
     */
    @NotNull(message = "请输入版本序号")
    private Integer versionCode;

    /**
     * 平台（android/ios/harmony）
     */
    @NotBlank(message = "请选择平台")
    @Pattern(regexp = AppPlatformEnum.REGEX, message = "不支持的 App 平台")
    private String platform;

    /**
     * 主包地址（android=apk 附件 path 或 HTTP(S) 直链；ios=外部跳转链接）
     */
    @NotBlank(message = "请上传安装包或填写下载地址")
    @Size(max = 500, message = "地址长度不能超过500个字符")
    private String downloadUrl;

    /**
     * 是否支持 wgt 热更新（0 否 / 1 是）
     */
    private String enableWgt;

    /**
     * 热更新地址（仅 android，enable_wgt=1 时上传或填写）
     */
    @Size(max = 500, message = "地址长度不能超过500个字符")
    private String wgtDownloadUrl;

    /**
     * 更新说明（纯文本多行）
     */
    @Size(max = 1000, message = "更新说明长度不能超过1000个字符")
    private String updateContent;

    /**
     * 状态（0 草稿 / 1 已发布 / 2 已下线）
     */
    private String status;
}
