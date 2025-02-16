package com.lihua.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class SysSetting implements Serializable {

    /**
     * 设置组件名称
     */
    @TableId(type = IdType.INPUT)
    private String settingComponentName;

    /**
     * 设置名称
     */
    private String settingName;

    /**
     * 设置json
     */
    private String settingJson;

}
