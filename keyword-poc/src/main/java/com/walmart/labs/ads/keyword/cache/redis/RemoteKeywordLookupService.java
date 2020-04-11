package com.walmart.labs.ads.keyword.cache.redis;

import com.walmart.labs.ads.keyword.Query;
import com.walmart.labs.ads.keyword.cache.KeywordLookupService;
import com.walmart.labs.ads.keyword.datatype.Tenant;
import com.walmart.labs.ads.keyword.db.redis.RedisBidService;
import com.walmart.labs.ads.keyword.model.ParsedKeywordBid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RemoteKeywordLookupService implements KeywordLookupService {
    private static final Logger log = LoggerFactory.getLogger(RemoteKeywordLookupService.class);

    @Autowired
    RedisBidService redisBidService;

    /**
     * Add ad group -> set OF ParsedKeywordBid into global map.
     * @param tenant
     * @param adGroupId
     * @param keywordBis
     */
    @Override
    public void addAdGroupKeywordBid(Tenant tenant, int adGroupId, Set<ParsedKeywordBid> keywordBis) {
        String key = tenant.toString() + adGroupId;
        redisBidService.put(key, keywordBis);
    }

    /**
     *
     * Lookup with a query string along with a ad group id set.
     * @param tenant
     * @param searchQuery
     * @param adGroupIds
     * @return
     */
    @Override
    public Map<Integer, ParsedKeywordBid> queryMatching(Tenant tenant, String searchQuery, Set<Integer> adGroupIds) {
        if (searchQuery.trim().isEmpty())   return Collections.EMPTY_MAP;
        Query query = new Query(searchQuery);
        Map<Integer, ParsedKeywordBid> bestBidsByAdGroup = new ConcurrentHashMap<>();
        adGroupIds.stream().forEach(adGroupId -> {
            String key = tenant.toString() + adGroupId;
            Optional<ParsedKeywordBid> best = bestBid(query, redisBidService.get(key));
            if (best.isPresent()) {
                bestBidsByAdGroup.put(adGroupId, best.get());
            }
        });

        return bestBidsByAdGroup;
    }

    /**
     * Simple search for keys.
     * @param tenant
     * @param adGroup
     * @return
     */
    @Override
    public Set<ParsedKeywordBid> search(Tenant tenant, int adGroup) {
        String key = tenant.toString() + adGroup;
        return new HashSet(redisBidService.get(key));
    }
}
