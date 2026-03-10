package com.lihua.ip.config;

import org.lionsoul.ip2region.xdb.LongByteArray;
import org.lionsoul.ip2region.xdb.Searcher;
import org.lionsoul.ip2region.xdb.Version;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * ip归属地
 */
@Configuration
public class Ip2regionConfig {

    @Bean
    public Searcher ipSearcher() throws Exception {
        ClassPathResource resource = new ClassPathResource("ip2region/ip2region_v4.xdb");

        // 拷贝到临时文件
        File tempFile = File.createTempFile("ip2region-", ".xdb");
        tempFile.deleteOnExit();

        try (InputStream in = resource.getInputStream();
             FileOutputStream out = new FileOutputStream(tempFile)) {
            in.transferTo(out);
        }

        LongByteArray longByteArray = Searcher.loadContentFromFile(tempFile.getAbsolutePath());
        return Searcher.newWithBuffer(Version.IPv4, longByteArray);
    }
}
