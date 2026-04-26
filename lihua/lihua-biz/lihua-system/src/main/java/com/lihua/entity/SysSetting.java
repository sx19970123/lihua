package com.lihua.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lihua.mybatis.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysSetting extends BaseEntity {

    /**
     * 设置key（前端vue组件名称）
     */
    @TableId(type = IdType.INPUT)
    private String settingKey;

    /**
     * 设置json
     */
    private String json;

}
