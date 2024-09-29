package com.lihua.interceptor;

import com.lihua.exception.IpIllegalException;
import com.lihua.system.entity.SysSetting;
import com.lihua.system.model.dto.SysSettingDTO;
import com.lihua.system.service.SysSettingService;
import com.lihua.utils.json.JsonUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class RequestIpInterceptor implements HandlerInterceptor {

    @Resource
    private SysSettingService sysSettingService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ipMatch(request.getRemoteAddr());
        return true;
    }

    // ip 匹配
    private void ipMatch(String currentIp) {
        // 系统中配置的禁止访问ip
        SysSetting restrictAccessIpSetting = sysSettingService.getSysSettingByComponentName("RestrictAccessIpSetting");
        // 没有此配置项直接返回
        if (restrictAccessIpSetting == null) {
            return;
        }
        SysSettingDTO.RestrictAccessIpSetting ipSetting = JsonUtils.toObject(restrictAccessIpSetting.getSettingJson(), SysSettingDTO.RestrictAccessIpSetting.class);
        // 未开启配置直接返回
        if (!ipSetting.isEnable()) {
            return;
        }
        List<String> prohibitIpList = ipSetting.getIpList();
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
