package com.lihua.strategy.system.authentication.impl.loginsetting;

import com.lihua.model.security.CurrentDept;
import com.lihua.model.security.LoginUser;
import com.lihua.strategy.system.authentication.CheckLoginSettingStrategy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 检查是否有默认部门
 */
@Component
@Order(3)
public class CheckDefaultDeptStrategyImpl implements CheckLoginSettingStrategy {

    final String COMPONENT_NAME = "LoginSettingDefaultDept";

    @Override
    public String checkSetting(LoginUser loginUser) {
        List<CurrentDept> deptList = loginUser.getDeptList();

        // 没有配置部门的用户
        if (deptList.isEmpty()) {
            return null;
        }

        // 判断默认部门是否存在
        List<CurrentDept> defDeptList = deptList.stream().filter(item -> "0".equals(item.getDefaultDept())).toList();
        if (defDeptList.isEmpty()) {
            return COMPONENT_NAME;
        }

        return null;
    }
}
