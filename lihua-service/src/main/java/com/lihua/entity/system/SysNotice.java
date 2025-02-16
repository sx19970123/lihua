package com.lihua.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lihua.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysNotice extends BaseEntity {

    /**
     * 主键 id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 类型
     */
    private String type;

    /**
     * 状态： 未发布 0 / 已发布 1 / 已撤销 2
     */
    private String status;

    /**
     * 优先级
     */
    private String priority;

    /**
     * 是否发送给全部用户
     */
    private String userScope;

    /**
     * 内容
     */
    private String content;

    /**
     * 发布时间
     */
    private LocalDateTime releaseTime;

    /**
     * 发布人id
     */
    private String releaseId;

    /**
     * 逻辑删除标识
     */
    private String delFlag;

    /**
     * 备注
     */
    private String remark;
}
