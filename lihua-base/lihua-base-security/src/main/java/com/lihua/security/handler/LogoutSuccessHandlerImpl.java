package com.lihua.security.handler;

import com.lihua.common.model.response.Responses;
import com.lihua.security.manager.LoginUserManager;
import com.lihua.security.model.LoginUserSession;
import com.lihua.web.utils.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, Authentication authentication) {
        String token = WebUtils.getToken(request);

        LoginUserSession loginUserSession = LoginUserManager.getLoginUser(token);

        if (loginUserSession != null && StringUtils.hasText(token)) {
            LoginUserManager.removeLoginUserCache(token);
        }

        WebUtils.renderJson(Responses.success("退出成功"));
    }

}
