package com.lihua.system.model.dto;

import com.lihua.model.BaseDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysNoticeDTO extends BaseDTO {
    /**
     * 主键 id
     */
    private String id;

    /**
     * 标题
     */
    @NotNull(message = "请输入标题")
    private String title;

    /**
     * 类型
     */
    @NotNull(message = "请选择类型")
    private String type;

    /**
     * 状态
     */
    @NotNull(message = "请选择状态")
    private String status;

    /**
     * 优先级
     */
    @NotNull(message = "请选择优先级")
    private String priority;

    /**
     * 内容
     */
    private String context;

    /**
     * 逻辑删除标识
     */
    private String delFlag;

    /**
     * 备注
     */
    private String remark;

    /**
     * 用户id集合
     */
    private List<String> userIdList;
}
