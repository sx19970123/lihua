package com.lihua.strategy.postlogincheck;

import com.lihua.security.model.CurrentDept;
import com.lihua.security.model.LoginUserSession;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 检查是否有默认部门
 */
@Component
@Order(3)
public class DefaultDeptStrategyImpl implements PostLoginCheckStrategy {

    final String COMPONENT_NAME = "UserSetupDefaultDept";

    @Override
    public String check(LoginUserSession loginUserSession) {
        List<CurrentDept> deptList = loginUserSession.getDeptList();

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
