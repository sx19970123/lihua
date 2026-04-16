package com.lihua.ip.utils;

import com.lihua.common.utils.spring.SpringUtils;
import com.lihua.common.utils.web.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.util.StringUtils;

public class IpUtils {

    /**
     * 获取当前请求的 ip地址
     * @return ip地址
     */
    public static String getIpAddress() {
        HttpServletRequest request = WebUtils.getCurrentRequest();
        String ip = request.getHeader("X-Forwarded-For");
        if (!StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (!StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
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
