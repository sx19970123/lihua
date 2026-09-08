package com.lihua.model.dto;

import com.lihua.mybatis.model.BaseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 通知公告已读/未读用户分页查询参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NoticeReadInfoDTO extends BaseDTO {

    /**
     * 通知公告 id
     */
    @NotBlank(message = "通知公告id不能为空")
    private String noticeId;

    /**
     * 已读标识：0 未读 / 1 已读
     */
    @NotBlank(message = "已读标识不能为空")
    @Pattern(regexp = "^[01]$", message = "已读标识不合法")
    private String readFlag;
}
