package com.lihua.strategy.postlogincheck;

import com.lihua.security.model.LoginUserSession;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 检查是否为自助注册并首次登录用户
 */
@Component
@Order(2)
public class PostNewUserBasicInfoStrategyImpl implements PostLoginCheckStrategy {

    final String COMPONENT_NAME = "PostLoginCheckUserBasics";

    @Override
    public String check(LoginUserSession loginUserSession) {
        String registerType = loginUserSession.getUser().getRegisterType();

        if ("0".equals(registerType)) {
            return null;
        }

        // 性别&昵称为 null 即认为是新注册用户
        if (loginUserSession.getUser().getNickname() == null && loginUserSession.getUser().getGender() == null) {
            return COMPONENT_NAME;
        }

        return null;
    }
}
