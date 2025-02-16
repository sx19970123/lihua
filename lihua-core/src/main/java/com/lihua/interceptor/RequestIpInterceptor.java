package com.lihua.interceptor;

import com.lihua.exception.IpIllegalException;
import com.lihua.service.system.setting.SysSettingService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class RequestIpInterceptor implements HandlerInterceptor {

    @Resource
    private SysSettingService sysSettingService;

    @Override
    public boolean preHandle(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response,@Nullable  Object handler) {
        if (request != null) {
            ipMatch(request.getRemoteAddr());
        }
        return true;
    }

    // ip 匹配
    private void ipMatch(String currentIp) {
        List<String> prohibitIpList = sysSettingService.getIpBlackList();
        // 使用正则匹配ip地址
        if (!prohibitIpList.isEmpty()) {
            prohibitIpList.forEach(ip -> {
                String regex = ip.replace("?", ".").replace("*", ".*");
                Pattern compiledPattern = Pattern.compile(regex);
                Matcher matcher = compiledPattern.matcher(currentIp);

                if (matcher.matches()) {
                    log.error("异常ip【{}】请求已拒绝", currentIp);
                    throw new IpIllegalException();
                }
            });
        }
    }
}
