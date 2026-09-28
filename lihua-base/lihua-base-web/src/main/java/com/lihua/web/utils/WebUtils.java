package com.lihua.web.utils;

import com.lihua.common.enums.CustomHttpHeader;
import com.lihua.common.enums.TokenEnum;
import com.lihua.common.utils.ip.IpResolveUtils;
import com.lihua.common.utils.spring.SpringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.util.Objects;

/**
 * web相关工具类
 */
@Slf4j
public class WebUtils {

    /**
     * 将 json 数据进行响应
     */
    public static void renderJson(String json) {
        renderJson(200, json);
    }

    /**
     * 将 json 数据进行响应
     */
    @SneakyThrows
    public static void renderJson(int code, String json) {
        HttpServletResponse response = getCurrentResponse();
        if (response == null) {
            log.error("响应数据写入失败，获取到的 HttpServletResponse 为空");
            return;
        }
        response.setStatus(code);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

    /**
     * 获取当前请求的 HttpServletRequest
     */
    public static HttpServletRequest getCurrentRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        return ((ServletRequestAttributes) Objects.requireNonNull(requestAttributes)).getRequest();
    }

    /**
     * 获取当前请求的 HttpServletResponse
     */
    public static HttpServletResponse getCurrentResponse() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        return ((ServletRequestAttributes) Objects.requireNonNull(requestAttributes)).getResponse();
    }

    /**
     * 获取客户端类型
     * @return web ｜ app ｜ app_h5 ｜ wechat_mp ｜ null
     */
    public static String getClientType() {
        HttpServletRequest request = getCurrentRequest();
        return getClientType(request);
    }

    /**
     * 根据request获取客户端类型
     */
    public static String getClientType(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        return request.getHeader(CustomHttpHeader.CLIENT_TYPE.getValue());
    }

    /**
     * 根据request获取token（去除 Bearer 前缀后的裸值）
     *
     * @return token；未携带或为空白时返回 null
     */
    public static String getToken(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String token = request.getHeader(TokenEnum.TOKEN_KEY.getValue());
        if (StringUtils.hasText(token)) {
            return token.replace(TokenEnum.TOKEN_PREFIX.getValue(), "").trim();
        }
        return null;
    }

    /**
     * 获取当前请求的客户端真实ip
     *
     * @return ip；非请求线程或全部信号不可用时返回 null
     */
    public static String getIpAddress() {
        return getIpAddress(getCurrentRequest());
    }

    /**
     * 根据request获取客户端真实ip
     * <p>
     * 单体无网关裁决层，按 X-Real-IP → X-Forwarded-For 最右合法段 → remoteAddr 三级解析
     *
     * @return ip；全部信号不可用时返回 null
     */
    public static String getIpAddress(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        return IpResolveUtils.resolveClientIp(request.getHeader("X-Real-IP"),
                request.getHeader("X-Forwarded-For"), request.getRemoteAddr());
    }

    /**
     * 根据ip地址查询归属地（ip2region）
     *
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
