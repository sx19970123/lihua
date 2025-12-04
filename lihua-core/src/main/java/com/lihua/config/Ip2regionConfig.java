package com.lihua.config;

import org.lionsoul.ip2region.xdb.LongByteArray;
import org.lionsoul.ip2region.xdb.Searcher;
import org.lionsoul.ip2region.xdb.Version;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

/**
 * ip归属地
 */
@Configuration
public class Ip2regionConfig {

    @Bean
    public Searcher ipSearcher() throws Exception {
        String dbPath = ResourceUtils.getFile("classpath:ip2region/ip2region_v4.xdb").getPath();
        LongByteArray longByteArray = Searcher.loadContentFromFile(dbPath);
        return Searcher.newWithBuffer(Version.IPv4, longByteArray);
    }
}
