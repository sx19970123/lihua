package com.lihua.common.utils.ip;

import java.util.regex.Pattern;

/**
 * 客户端真实 IP 解析：X-Real-IP（前置代理覆写语义，单值）→ X-Forwarded-For（追加语义，取最右侧合法段，由最内层代理写入）→ remoteAddr（TCP 对端）三级回退。
 * 全程防御式：任一信号缺失/unknown/格式非法即跳过回退，任何输入不抛错；三级信号全部不可用时返回 null。
 * 合法性用正则校验而非 InetAddress——后者对非 IP 字符串会发起 DNS 查询，畸形头可被用来触发解析阻塞。
 */
public final class IpResolveUtils {

    /**
     * IPv4 点分十进制（每段 0-255，不允许前导零）
     */
    private static final Pattern IPV4 = Pattern.compile("^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$");

    /**
     * IPv6 宽松校验（仅约束字符集与长度；畸形值只是匹配不到黑名单规则，无危害）
     */
    private static final Pattern IPV6 = Pattern.compile("^[0-9a-fA-F:]{2,45}$");

    private IpResolveUtils() {
    }

    /**
     * 解析客户端真实 IP
     *
     * @param xRealIp      X-Real-IP 头（标准反代为覆写语义，客户端自带的伪造值会被替换）
     * @param forwardedFor X-Forwarded-For 头原始值（追加语义：最右侧段由最内层代理写入，客户端仅能污染左侧段）
     * @param remoteAddr   TCP 对端地址（无代理时即客户端 IP，绝对真实）
     * @return 解析出的 IP；三级信号全部不可用时返回 null
     */
    public static String resolveClientIp(String xRealIp, String forwardedFor, String remoteAddr) {
        String ip = normalize(xRealIp);
        if (ip != null) {
            return ip;
        }
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            String[] segments = forwardedFor.split(",");
            for (int i = segments.length - 1; i >= 0; i--) {
                ip = normalize(segments[i]);
                if (ip != null) {
                    return ip;
                }
            }
        }
        return normalize(remoteAddr);
    }

    private static String normalize(String candidate) {
        if (candidate == null || candidate.isBlank()) {
            return null;
        }
        String ip = candidate.trim();
        if ("unknown".equalsIgnoreCase(ip)) {
            return null;
        }
        if (IPV4.matcher(ip).matches()) {
            return ip;
        }
        return ip.indexOf(':') >= 0 && IPV6.matcher(ip).matches() ? ip : null;
    }
}
