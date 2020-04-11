package com.walmart.labs.ads.keyword.cache.caffeine;

import com.walmart.labs.ads.keyword.cache.KeywordLookupService;
import com.walmart.labs.ads.keyword.datatype.Tenant;
import com.walmart.labs.ads.keyword.model.ParsedKeywordBid;

import java.util.Map;
import java.util.Set;

public class CaffeineKeywordLookupService implements KeywordLookupService {
    @Override
    public void addAdGroupKeywordBid(Tenant tenant, int adGroupId, Set<ParsedKeywordBid> keywordBis) {

    }

    @Override
    public Map<Integer, ParsedKeywordBid> queryMatching(Tenant tenant, String searchQuery, Set<Integer> adGroupIds) {
        return null;
    }

    @Override
    public Set<ParsedKeywordBid> search(Tenant tenant, int adGroup) {
        return null;
    }
}
