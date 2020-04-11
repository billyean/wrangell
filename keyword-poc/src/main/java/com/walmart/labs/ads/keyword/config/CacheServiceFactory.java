package com.walmart.labs.ads.keyword.config;

import com.walmart.labs.ads.keyword.cache.KeywordLookupService;
import com.walmart.labs.ads.keyword.cache.ehcache.MulltiTierKeywordLookupService;
import com.walmart.labs.ads.keyword.cache.redis.RemoteKeywordLookupService;
import com.walmart.labs.ads.keyword.cache.simple.SimpleKeywordLookupService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheServiceFactory {
    /**
     * A setting for service name.
     */
    private @Value("${keywordLookupService.name}") String serviceName;

    /**
     * The root folder for store the persistent cache data.
     */
    private @Value("${keywordLookupService.persistent.folder}") String persistentStorage;

    @Bean
    public KeywordLookupService getMultiTierKeywordLookupService() {
        return new MulltiTierKeywordLookupService(persistentStorage);
    }

    @Bean
    public KeywordLookupService getRemoteKeywordLookupService() {
        return new RemoteKeywordLookupService();
    }

    @Bean
    public KeywordLookupService getSimpleKeywordLookupService() {
        return new SimpleKeywordLookupService();
    }

    @Bean
    public KeywordLookupService getService(){
        switch (serviceName) {
            case "simpleKeywordLookupService":
                return getSimpleKeywordLookupService();
            case "multiTierKeywordLookupService":
                return getMultiTierKeywordLookupService();
            default:
                return getRemoteKeywordLookupService();
        }
    }
}
