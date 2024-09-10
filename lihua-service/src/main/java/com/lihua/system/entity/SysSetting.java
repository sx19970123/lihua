package com.lihua.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class SysSetting {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 设置名称
     */
    private String settingName;

    /**
     * 设置组件名称
     */
    private String settingComponentName;

    /**
     * 设置json
     */
    private String settingJson;

}
