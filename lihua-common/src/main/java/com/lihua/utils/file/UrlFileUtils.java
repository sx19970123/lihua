package com.lihua.utils.file;

import com.lihua.exception.FileException;
import inet.ipaddr.IPAddress;
import inet.ipaddr.IPAddressString;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
public class UrlFileUtils {

    // IPv6 匹配
    private static final Pattern IPV6_ULA = Pattern.compile("^fc[0-9a-f]{2}:.*", Pattern.CASE_INSENSITIVE);

    // ip 黑名单
    private static final List<IPAddress> DEFAULT_BLOCKED_CIDRS = List.of(
            new IPAddressString("10.0.0.0/8").getAddress(),
            new IPAddressString("172.16.0.0/12").getAddress(),
            new IPAddressString("192.168.0.0/16").getAddress(),
            new IPAddressString("127.0.0.0/8").getAddress(),
            new IPAddressString("169.254.0.0/16").getAddress(),
            new IPAddressString("169.254.169.254/32").getAddress(),
            new IPAddressString("100.100.100.200/32").getAddress(),
            new IPAddressString("169.254.0.23/32").getAddress(),
            new IPAddressString("::1/128").getAddress(),
            new IPAddressString("fe80::/10").getAddress(),
            new IPAddressString("fc00::/7").getAddress()
    );

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
        checkUrlSafely(uri);
        String contentType;
        int size;
        // 通过读取InputStream获取附件size和contentType
        try (InputStream inputStream = uri.openStream()) {
            contentType = uri.openConnection().getContentType();
            size = inputStream.available();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new FileException("读取URL失败");
        }

        final URL finalUrl = uri;

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
                return contentType;
            }

            @Override
            public boolean isEmpty() {
                return size == 0;
            }

            @Override
            public long getSize() {
                return size;
            }

            @NotNull
            @Override
            public byte[] getBytes() throws IOException {
                try (InputStream inputStream = finalUrl.openStream()) {
                    return inputStream.readAllBytes();
                }
            }

            @NotNull
            @Override
            public InputStream getInputStream() throws IOException {
                return finalUrl.openStream();
            }

            @Override
            public void transferTo(@NotNull File dest) throws IOException, IllegalStateException {
                try (InputStream inputStream = finalUrl.openStream()) {
                    Files.copy(inputStream, dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        };
    }

    /**
     * 检查 URL 安全性
     */
    @SneakyThrows
    private static void checkUrlSafely(URL uri) {

        // 仅允许 https|http 请求
        String protocol = uri.getProtocol();
        if (!"https".equalsIgnoreCase(protocol) && !"http".equalsIgnoreCase(protocol)) {
            throw new FileException("读取URL失败，非法的协议" + protocol);
        }

        String host = uri.getHost();
        // 仅允许存在host的链接
        if (!StringUtils.hasText(host)) {
            throw new FileException("读取URL失败，请求链接没有host");
        }

        InetAddress[] allByName = InetAddress.getAllByName(host);

        for (InetAddress addr : allByName) {
            if (addr.isAnyLocalAddress() || addr.isLoopbackAddress() || addr.isLinkLocalAddress() || addr.isSiteLocalAddress() || addr.isMulticastAddress()) {
                throw new FileException("读取URL失败，限制访问的目标地址");
            }

            if (IPV6_ULA.matcher(addr.getHostAddress()).matches()) {
                throw new FileException("读取URL失败，限制访问的目标地址");
            }

            if (isBlocked(addr.getHostAddress())) {
                throw new FileException("读取URL失败，限制访问的目标地址");
            }
        }
    }

    /**
     * 是否为ip黑名单
     */
    private static boolean isBlocked(String ipStr) {
        IPAddress ip = new IPAddressString(ipStr).getAddress();
        for (IPAddress block : DEFAULT_BLOCKED_CIDRS) {
            if (block.contains(ip)) {
                return true;
            }
        }
        return false;
    }
}
