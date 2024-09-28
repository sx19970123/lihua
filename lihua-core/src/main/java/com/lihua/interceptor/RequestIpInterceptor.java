package com.lihua.interceptor;

import com.lihua.exception.IpIllegalException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class RequestIpInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ipMatch(request.getRemoteAddr());
        return true;
    }

    // ip 匹配
    private void ipMatch(String currentIp) {
        // 系统中配置的禁止访问ip
        List<String> prohibitIpList = new ArrayList<>();
        // prohibitIpList.add("0:0:0:0:0:0:0:1");
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
