package utils.web;

import enums.ConstantEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import utils.spring.SpringUtils;

import java.util.Objects;

/**
 * web相关工具类
 */
public class WebUtils {

    /**
     * 将 json 数据进行响应
     */
    @SneakyThrows
    public static void renderJson(HttpServletResponse response, String json) {
        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

    /**
     * 获取当前请求的HttpServletRequest
     */
    public static HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    /**
     * 从请求中获取 token
     */
    public static String getToken(HttpServletRequest request) {
        // 获取 token
        String token = null;
        if (request != null) {
            token = request.getHeader(ConstantEnum.TOKEN_KEY.getValue());
        }
        if (StringUtils.hasText(token)) {
            return token.replace(ConstantEnum.TOKEN_PREFIX.getValue(), "").trim();
        }

        return null;
    }

    /**
     * 获取当前请求的 ip地址
     * @return ip地址
     */
    public static String getIpAddress() {
        HttpServletRequest request = getCurrentRequest();
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

    /**
     * 获取客户端类型
     * @return web ｜ app ｜ wechat_mp ｜ null
     */
    public static String getClientType() {
        HttpServletRequest request = getCurrentRequest();
        return request.getHeader("Client-Type");
    }

}
