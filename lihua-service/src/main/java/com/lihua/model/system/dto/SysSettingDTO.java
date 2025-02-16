package com.lihua.model.system.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统设置实体类
 * 用于配置JSON转换后的数据接收
 */
@Data
public class SysSettingDTO implements Serializable {

    /**
     * 设置开关
     */
    private boolean enable;

    /**
     * 默认密码
     */
    @Data
    public static class DefaultPasswordSetting implements Serializable {
        // 系统默认密码
        private String defaultPassword;
    }

    /**
     * 限制访问ip
     */
    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class RestrictAccessIpSetting extends SysSettingDTO {
        List<String> ipList;
    }

    /**
     * 同账号登录数量
     */
    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class SameAccountLoginSetting extends SysSettingDTO {
        int maximum;
    }

    /**
     * 定期修改密码
     */
    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class IntervalUpdatePasswordSetting extends SysSettingDTO {
        // 值
        private Integer interval;
        // 单位
        private String unit;
    }

    /**
     * 自助注册
     */
    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class SignInSetting extends SysSettingDTO {
        // 部门id集合
        private List<String> deptIds;
        // 默认部门id
        private String defaultDeptId;
        // 岗位id集合
        private List<String> postIds;
        // 角色id集合
        private List<String> roleIds;
    }

    /**
     * 灰色模式
     */
    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class GrayModelSetting extends SysSettingDTO {
        // 自动关闭时间
        private LocalDateTime closeTime;
    }
}
