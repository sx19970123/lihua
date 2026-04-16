package com.lihua.security.handler;

import com.lihua.common.model.response.basecontroller.StrResponseController;
import com.lihua.common.utils.web.WebUtils;
import com.lihua.security.manager.LoginUserManager;
import com.lihua.security.model.LoginUserSession;
import com.lihua.security.utils.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class LogoutSuccessHandlerImpl extends StrResponseController implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, Authentication authentication) {
        String token = TokenUtils.getToken(request);

        LoginUserSession loginUserSession = LoginUserManager.getLoginUser(token);

        if (loginUserSession != null) {
            LoginUserManager.removeLoginUserCache(token);
        }

        WebUtils.renderJson(success("退出成功"));
    }

}
