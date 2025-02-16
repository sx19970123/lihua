package com.lihua.strategy.system.authentication.impl.loginsetting;

import com.lihua.model.security.LoginUser;
import com.lihua.strategy.system.authentication.CheckLoginSettingStrategy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 检查是否为自助注册并首次登录用户
 */
@Component
@Order(2)
public class CheckNewUserBasicInfoStrategyImpl implements CheckLoginSettingStrategy {

    final String COMPONENT_NAME = "LoginSettingUserBasics";

    @Override
    public String checkSetting(LoginUser loginUser) {
        String registerType = loginUser.getUser().getRegisterType();

        if ("0".equals(registerType)) {
            return null;
        }

        // 性别&昵称为 null 即认为是新注册用户
        if (loginUser.getUser().getNickname() == null && loginUser.getUser().getGender() == null) {
            return COMPONENT_NAME;
        }

        return null;
    }
}
