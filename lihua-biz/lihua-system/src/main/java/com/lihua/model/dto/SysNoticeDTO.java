package com.lihua.model.dto;

import com.lihua.mybatis.model.BaseDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @Size(max = 100, message = "标题长度不能超过100个字符")
    private String title;

    /**
     * 类型
     */
    @NotNull(message = "请选择类型")
    @Pattern(regexp = "^[01]$", message = "公告类型不合法")
    private String type;

    /**
     * 状态
     */
    @NotNull(message = "请选择状态")
    @Pattern(regexp = "^[012]$", message = "公告状态不合法")
    private String status;

    /**
     * 优先级
     */
    @NotNull(message = "请选择优先级")
    @Pattern(regexp = "^[0-3]$", message = "优先级别不合法")
    private String priority;

    /**
     * 是否发送给全部用户
     */
    @NotNull(message = "请选择用户范围")
    @Pattern(regexp = "^[01]$", message = "用户范围不合法")
    private String userScope;

    /**
     * 内容
     */
    private String content;

    /**
     * 逻辑删除标识
     */
    private String delFlag;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;

    /**
     * 用户id集合
     */
    private List<String> userIdList;

    /**
     * star 数据
     */
    private String star;

    /**
     * 图标
     */
    @Size(max = 100, message = "图标长度不能超过100个字符")
    private String icon;
}
