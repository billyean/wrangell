package com.walmart.labs.ads.keyword.cache.simple;

import com.walmart.labs.ads.keyword.Query;
import com.walmart.labs.ads.keyword.cache.KeywordLookupService;
import com.walmart.labs.ads.keyword.datatype.Tenant;
import com.walmart.labs.ads.keyword.model.ParsedKeywordBid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import static com.walmart.labs.ads.keyword.datatype.Tenant.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleKeywordLookupService implements KeywordLookupService {
    private static final Logger log = LoggerFactory.getLogger(SimpleKeywordLookupService.class);

    /**
     * A enum map that have all adgroup -> Set<ParsedKeywordBid>.
     */
    private final EnumMap<Tenant, Map<Integer, Set<ParsedKeywordBid>>> tenantWordAdGroups = new EnumMap(Tenant.class){{
        this.put(WMT, new ConcurrentHashMap<>());
        this.put(GR, new ConcurrentHashMap<>());
    }};

    /**
     * Add ad group -> set OF ParsedKeywordBid into global map.
     * @param tenant
     * @param adGroupId
     * @param keywordBis
     */
    @Override
    public void addAdGroupKeywordBid(Tenant tenant, int adGroupId, Set<ParsedKeywordBid> keywordBis) {
        Map<Integer, Set<ParsedKeywordBid>> tenantAdGroupBids = tenantWordAdGroups.get(tenant);
        tenantAdGroupBids.put(adGroupId, keywordBis);
    }

    /**
     *
     * Lookup with a query string along with a ad group id list.
     * @param tenant
     * @param searchQuery
     * @param adGroupIds
     * @return
     */
    @Override
    public Map<Integer, ParsedKeywordBid> queryMatching(Tenant tenant, String searchQuery, Set<Integer> adGroupIds) {
        if (searchQuery.trim().isEmpty())   return Collections.EMPTY_MAP;
        Query query = new Query(searchQuery);

        Map<Integer, Set<ParsedKeywordBid>> groupBids = tenantWordAdGroups.get(tenant);
        Map<Integer, ParsedKeywordBid> bestBidsByAdGroup= new ConcurrentHashMap<>();
        adGroupIds.stream().forEach(adGroupId -> {
            Optional<ParsedKeywordBid> best = bestBid(query, groupBids.get(adGroupId));
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
        Map<Integer, Set<ParsedKeywordBid>> groupBids = tenantWordAdGroups.get(tenant);
        return tenantWordAdGroups.get(tenant).getOrDefault(adGroup, Collections.emptySet());
    }
}
