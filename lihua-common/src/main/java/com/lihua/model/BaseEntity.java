package com.lihua.model;

import com.github.liaochong.myexcel.core.annotation.IgnoreColumn;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 业务实体对象的基类
 */
@Data
public class BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 创建人
     */
    @IgnoreColumn
    private String createId;

    /**
     * 创建时间
     */
    @IgnoreColumn
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @IgnoreColumn
    private String updateId;

    /**
     * 更新时间
     */
    @IgnoreColumn
    private LocalDateTime updateTime;
}
