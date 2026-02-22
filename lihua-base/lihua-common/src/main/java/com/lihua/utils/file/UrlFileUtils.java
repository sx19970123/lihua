package com.lihua.utils.file;

import com.lihua.exception.FileException;
//import inet.ipaddr.IPAddress;
//import inet.ipaddr.IPAddressString;
import jakarta.validation.constraints.NotNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
public class UrlFileUtils {

    // IPv6 匹配
    private static final Pattern IPV6_ULA = Pattern.compile("^fc[0-9a-f]{2}:.*", Pattern.CASE_INSENSITIVE);

    // ip 黑名单
//    private static final List<IPAddress> DEFAULT_BLOCKED_CIDRS = List.of(
//            new IPAddressString("10.0.0.0/8").getAddress(),
//            new IPAddressString("172.16.0.0/12").getAddress(),
//            new IPAddressString("192.168.0.0/16").getAddress(),
//            new IPAddressString("127.0.0.0/8").getAddress(),
//            new IPAddressString("169.254.0.0/16").getAddress(),
//            new IPAddressString("169.254.169.254/32").getAddress(),
//            new IPAddressString("100.100.100.200/32").getAddress(),
//            new IPAddressString("169.254.0.23/32").getAddress(),
//            new IPAddressString("::1/128").getAddress(),
//            new IPAddressString("fe80::/10").getAddress(),
//            new IPAddressString("fc00::/7").getAddress()
//    );

    /**
     * url附件上传
     * @param url 附件链接
     */
    public static String upload(String url) {
        // 判断url是否合法
        if (!StringUtils.hasText(url)) {
            throw new FileException("附件URL不存在");
        }
        MultipartFile multipartFile = urlToMultipartFile(url);
        return FileUtils.upload(multipartFile);
    }

    /**
     * 将 URL转为 MultipartFile
     * @param url 附件链接
     * @return MultipartFile对象
     */
    public static MultipartFile urlToMultipartFile(String url) {
        URL uri;
        try {
            uri = new URL(url);
        } catch (MalformedURLException e) {
            log.error(e.getMessage(), e);
            throw new FileException("读取URL失败");
        }
        // 检查 url 是否正确
        // checkUrlSafely(uri);

        // 建立连接，获取contentType
        String contentType;
        URLConnection connection;

        try {
            connection = uri.openConnection();
            // 模拟浏览器，防止某些网站进行检查
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Connection", "keep-alive");
            // 获取ContentType
            contentType = connection.getContentType();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new FileException("读取URL失败");
        }

        // 获取缓存字节
        byte[] cachedBytes;
        try (InputStream inputStream = connection.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            cachedBytes = baos.toByteArray();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new FileException("读取URL失败");
        }

        final URL finalUrl = uri;
        final String finalContentType = contentType;

        // 创建 MultipartFile 对象
        return new MultipartFile() {
            @NotNull
            @Override
            public String getName() {
                return "file";
            }

            @Override
            public String getOriginalFilename() {
                return Paths.get(finalUrl.getPath()).getFileName().toString();
            }

            @Override
            public String getContentType() {
                return finalContentType;
            }

            @Override
            public boolean isEmpty() {
                return cachedBytes.length == 0;
            }

            @Override
            public long getSize() {
                return cachedBytes.length;
            }

            @NotNull
            @Override
            public byte[] getBytes() throws IOException {
                return cachedBytes;
            }

            @NotNull
            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(cachedBytes);
            }

            @Override
            public void transferTo(@NotNull File dest) throws IOException, IllegalStateException {
                try (OutputStream out = new FileOutputStream(dest)) {
                    out.write(cachedBytes);
                }
            }
        };
    }

    /**
     * 检查 URL 安全性
     */
//    @SneakyThrows
//    private static void checkUrlSafely(URL uri) {
//
//        // 仅允许 https|http 请求
//        String protocol = uri.getProtocol();
//        if (!"https".equalsIgnoreCase(protocol) && !"http".equalsIgnoreCase(protocol)) {
//            throw new FileException("读取URL失败，非法的协议" + protocol);
//        }
//
//        String host = uri.getHost();
//        // 仅允许存在host的链接
//        if (!StringUtils.hasText(host)) {
//            throw new FileException("读取URL失败，请求链接没有host");
//        }
//
//        InetAddress[] allByName = InetAddress.getAllByName(host);
//
//        for (InetAddress addr : allByName) {
//            if (addr.isAnyLocalAddress() || addr.isLoopbackAddress() || addr.isLinkLocalAddress() || addr.isSiteLocalAddress() || addr.isMulticastAddress()) {
//                throw new FileException("读取URL失败，限制访问的目标地址");
//            }
//
//            if (IPV6_ULA.matcher(addr.getHostAddress()).matches()) {
//                throw new FileException("读取URL失败，限制访问的目标地址");
//            }
//
//            if (isBlocked(addr.getHostAddress())) {
//                throw new FileException("读取URL失败，限制访问的目标地址");
//            }
//        }
//    }

    /**
     * 是否为ip黑名单
     */
//    private static boolean isBlocked(String ipStr) {
//        IPAddress ip = new IPAddressString(ipStr).getAddress();
//        for (IPAddress block : DEFAULT_BLOCKED_CIDRS) {
//            if (block.contains(ip)) {
//                return true;
//            }
//        }
//        return false;
//    }
}
