package com.lihua.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lihua.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
     * 状态： 未发布/已发布/已撤销
     */
    private String status;

    /**
     * 优先级
     */
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
}
