package com.lihua.system.enums;

import com.lihua.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BusinessUserEnums {
    ROLE("role","sys_user_role","role_id"),
    DEPT("dept","sys_user_dept","dept_id"),
    POST("post","sys_user_post","post_id");

    // 业务编码
    private final String businessCode;
    // 对应业务表
    private final String businessTableName;
    // 对应业务列名
    private final String businessFieldName;

    /**
     * 根据业务编码获取对应表名
     * @param businessCode
     * @return
     */
    public static String getTableNameByCode(String businessCode) {
        for (int i = 0; i < values().length; i++) {
            BusinessUserEnums businessUserEnum =values()[i];
            if (businessUserEnum.businessCode.equals(businessCode)) {
                return businessUserEnum.businessTableName;
            }
        }
        throw new ServiceException("业务类型错误");
    }

    /**
     * 根据业务编码获取对应列名
     * @param businessCode
     * @return
     */
    public static String getFieldNameByCode(String businessCode) {
        for (int i = 0; i < values().length; i++) {
            BusinessUserEnums businessUserEnum =values()[i];
            if (businessUserEnum.businessCode.equals(businessCode)) {
                return businessUserEnum.businessFieldName;
            }
        }
        throw new ServiceException("业务类型错误");
    }
}
