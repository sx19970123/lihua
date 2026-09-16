package com.lihua.ip.utils;

import com.lihua.common.utils.ip.IpResolveUtils;
import com.lihua.common.utils.spring.SpringUtils;
import com.lihua.web.utils.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;

@Slf4j
public class IpUtils {

    /**
     * 获取当前请求的 ip地址
     * @return ip地址
     */
    public static String getIpAddress() {
        HttpServletRequest currentRequest = WebUtils.getCurrentRequest();
        if (currentRequest == null) {
            log.error("获取ip地址失败，获取到的 HttpServletResponse 为空");
            return "";
        }
        return getIpAddress(currentRequest);
    }

    /**
     * 根据请求获取ip（X-Real-IP → X-Forwarded-For 末段 → remoteAddr 三级回退，解析规则见 IpResolveUtils）
     */
    public static String getIpAddress(HttpServletRequest request) {
        return IpResolveUtils.resolveClientIp(request.getHeader("X-Real-IP"), request.getHeader("X-Forwarded-For"), request.getRemoteAddr());
    }

    /**
     * 根据ip地址查询归属地
     * @param ip 地址
     * @return ip所属地区
     */
    public static String getRegion(String ip) {
        Searcher searcher = SpringUtils.getBean(Searcher.class);
        try {
            String search = searcher.search(ip);
            if (search.contains("内网")) {
                return "内网IP";
            }
            // 解析字符串，返回：国家 省份 城市
            String[] searchers = search.split("\\|");
            return (searchers[0] + " " + searchers[1] + " " + searchers[2]).replaceAll("\\b0\\b", "").replaceAll("\\s+", " ").trim();
        } catch (Exception e) {
            return "未知IP";
        }
    }
}
