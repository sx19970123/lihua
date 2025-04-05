package com.lihua.handle;

import com.lihua.model.security.LoginUser;
import com.lihua.model.web.basecontroller.StrResponseController;
import com.lihua.utils.security.LoginUserManager;
import com.lihua.utils.web.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class LogoutSuccessHandlerImpl extends StrResponseController implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = WebUtils.getToken(request);

        LoginUser loginUser = LoginUserManager.getLoginUser(token);

        if (loginUser != null) {
            LoginUserManager.removeLoginUserCache(token);
        }

        WebUtils.renderJson(response, success("退出成功"));
    }
}
