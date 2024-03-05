package com.lihua.handle;

import com.lihua.constant.Constant;
import com.lihua.model.security.LoginUser;
import com.lihua.model.web.BaseController;
import com.lihua.utils.security.LoginUserMgmt;
import com.lihua.utils.web.WebUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LogoutSuccessHandlerImpl extends BaseController implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String token = request.getHeader(Constant.TOKEN.getCode());
        LoginUser loginUser = LoginUserMgmt.getLoginUser(token);

        if (loginUser != null) {
            LoginUserMgmt.removeLoginUserCache(token);
        }

        WebUtils.renderJson(response, success("退出成功"));
    }
}
